# -*- coding: UTF-8 -*-

#import requests 
import glob 
import requests
import os
import sys

PGYER_AKEY = "1423174f786fcc390e8f8a80e0ac7d3d" 
PGYER_UKEY = "f76df97fb9a4bb30a099593cd9c63aae"
# 工作内容
WORKSPACE = ""
# APK 路径
APK_PATH = ""

def upload_apk():
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

    print("准备上传的APK: ", apk)

    # 上传apk
    url = 'https://www.pgyer.com/apiv1/app/upload'
    header = {'Content-Type': 'multipart/form-data'}
    files = {'file': open(apk, 'rb')}
    data = {
        "_api_key": PGYER_AKEY,
        "uKey": PGYER_UKEY,
        "buildUpdateDescription": "test",
        "updateDescription": "test"
    }
    print("正在上传APK 请稍后... ")
    response = requests.post(url, data=data, files=files)
    print("上传完毕")

def build_apk(build_type):
    apk_list=glob.glob(APK_PATH)
    for apk in apk_list :
        os.remove(apk)

    cmd = " ./gradlew a{}".format(build_type)
    os.system(cmd)

if __name__ == '__main__':

    WORKSPACE = os.path.abspath(os.path.dirname(sys.path[0]))
    APK_PATH = WORKSPACE + "/app/build/outputs/apk/*/*.apk"

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



