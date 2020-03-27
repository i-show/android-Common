# -*- coding: UTF-8 -*-

import glob 
import requests
import os
import sys
import json
import time
import shutil

PGYER_AKEY = "1423174f786fcc390e8f8a80e0ac7d3d"
PGYER_UKEY = "f76df97fb9a4bb30a099593cd9c63aae"
# 工作内容
WORKSPACE = ""
# APK 路径
APK_PATH = ""
OUR_PATH = ""
OUR_APK_PATH = ""
OUR_JIAGU_APK_PATH = ""

def timestamp_to_time(timestamp):
    """把时间戳转化为时间: 1479264792 to 2016-11-16 10:53:12"""
    return time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(timestamp))


def get_file_modify_time(file_path):
    """获取文件的修改时间"""
    # filePath = unicode(filePath, 'utf8')
    t = os.path.getmtime(file_path)
    return timestamp_to_time(t)

def jia_gu(apk):
    # shutil.rmtree(OUR_JIAGU_APK_PATH)
    if not os.path.exists(OUR_JIAGU_APK_PATH):
        os.makedirs(OUR_JIAGU_APK_PATH)
    
    cmd = "java -jar {}/utils/jiagu/jiagu.jar -jiagu {} {} -autosign".format(WORKSPACE, apk, OUR_JIAGU_APK_PATH)

    print("jiagu cmd = " , cmd)
    os.system(cmd)

    apk_list = os.listdir(OUR_JIAGU_APK_PATH)
    if len(apk_list) < 1 :
        return apk
    else :
        return OUR_JIAGU_APK_PATH + "/" + apk_list[0]   


def upload_apk():
    print("search cmd = ", APK_PATH)
    apk_list=glob.glob(APK_PATH, recursive=True)
    apk_len=len(apk_list)

    if apk_len <= 0 :
        print("没有发现APK")
        return

    if apk_len > 1:
        index = 0
        for apk in apk_list :
            print("[{}] {}".format(index, apk))
            index = index + 1

        indexStr = input("发现多个APK，请选择您要上传的APK \n")
        apk = apk_list[int(indexStr)]
    else:
        apk = apk_list[0]

    apk_name = os.path.split(apk)[-1]
    if "sit" in apk_name :
        app_type = "测试版本"
    elif "uat" in apk_name :
        app_type = "测试版本"
    elif "prod" in apk_name :
        app_type = "生产版本"

    print("准备上传的APK: ", apk_name)

    build_date = get_file_modify_time(apk)
    upload_time = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())
    upload_des = "{} \n更新内容: 修复了若干Bug \n下载地址: https://www.pgyer.com/adunpai \n编译时间: {}\n上传时间: {}".format(app_type, build_date, upload_time)
   
    apk = jia_gu(apk)
    print("准备上传的APK: ", apk)

    # 上传apk
    url = 'https://www.pgyer.com/apiv1/app/upload'
    header = {'Content-Type': 'multipart/form-data'}
    files = {'file': open(apk, 'rb')}
    data = {
        "_api_key": PGYER_AKEY,
        "uKey": PGYER_UKEY,
        "buildUpdateDescription": upload_des,
        "updateDescription": upload_des
    }
    
    print("正在上传APK 请稍后... ")
    response = requests.post(url, data=data, files=files)
    print("上传完毕")

    response = json.loads(response.text)
    app_version = response["data"]["appVersion"]
    
    feishu_title = "A盾牌V{} {}发布".format(app_version, app_type)

    data = {
        "title": feishu_title,
        "text": upload_des,
    }

    data = json.dumps(data)
    url = "https://open.feishu.cn/open-apis/bot/hook/834378cd5cf145649e9ce2f5ee667d92"
    header = {'Content-Type': 'application/json'}
    # response = requests.post(url, data=data, headers=header)
    
def build_apk(build_type):
    apk_list=glob.glob(APK_PATH)
    for apk in apk_list :
        os.remove(apk)

    cmd = " ./gradlew a{}".format(build_type)
    os.system(cmd)

if __name__ == '__main__':
    WORKSPACE = os.path.abspath(os.path.dirname(sys.path[0]))
    OUR_PATH = WORKSPACE + "/out"
    OUR_APK_PATH = OUR_PATH + "/apk"
    OUR_JIAGU_APK_PATH = OUR_APK_PATH + "/jiagu"

    APK_PATH = WORKSPACE + "/app/build/outputs/apk/**/*.apk"

    arg_list = sys.argv
    
    need_build = False
    build_type = "Uat"

    for arg in arg_list :
        if arg == 'b' or arg == 'build':
            need_build = True

        if arg.lower() == "sit" :
            build_type = "Sit"

        if arg.lower() == "uat" :
            build_type = "Uat"

        if arg.lower() == "prod" or arg.lower() == "release" or arg.lower() == "r":
            build_type = "R"

    if need_build :
        build_apk(build_type)

    upload_apk()


