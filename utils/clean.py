# -*- coding: UTF-8 -*-
# EEE
import os
import sys
import shutil

path = sys.path[0]


def delet_dir(path):
    files = os.listdir

for root, dirs, files in os.walk(path, topdown=False):
    for name in files:
        if name.endswith('.iml'):
            os.remove(os.path.join(root, name))

        if name == "local.properties":
            os.remove(os.path.join(root, name))

    for dir in dirs:
        if dir == "build":
            shutil.rmtree(os.path.join(root, dir))

        if dir == ".gradle":
            shutil.rmtree(os.path.join(root, dir))

        if dir == ".idea":
            shutil.rmtree(os.path.join(root, dir))
