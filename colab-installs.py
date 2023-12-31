#The code below installs 3.9 (assuming you now have 3.10) and restarts environment, so you can run your cells.

import sys #for version checker
import os #for restart routine

if '3.9' in sys.version:
  print('You already have 3.9')
else:
  #install python 3.9 and dev utils
  #you may not need all the dev libraries, but I haven't tested which aren't necessary.
  !sudo apt-get update -y
  !sudo apt-get install python3.9 python3.9-dev python3.9-distutils libpython3.9-dev 
  !sudo apt-get install python3.9-venv binfmt-support #recommended in install logs of the command above

  #change alternatives
  !sudo update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.10 1
  !sudo update-alternatives --install /usr/bin/python3 python3 /usr/bin/python3.9 2

  # install pip
  !curl -sS https://bootstrap.pypa.io/get-pip.py | python3.9
  !python3 get-pip.py --force-reinstall

  #install colab's dependencies
  !python3 -m pip install setuptools ipython ipython_genutils ipykernel jupyter_console prompt_toolkit httplib2 astor

  #minor cleanup
  !sudo apt autoremove

  #link to the old google package
  !ln -s /usr/local/lib/python3.10/dist-packages/google /usr/local/lib/python3.9/dist-packages/google
  #this is just to verify if 3.9 folder was indeed created
  !ls /usr/local/lib/python3.9/

  !pip install google-colab==1.0.0
# install colab's dependencies
  !python -m pip install ipython==7.9.0 ipython_genutils==0.2.0 ipykernel==5.3.4 jupyter_console==6.1.0 prompt_toolkit==2.0.10 httplib2==0.17.4 astor==0.8.1 traitlets==5.7.1 google==2.0.3

!sudo apt -y install libportaudio2
!pip -v install -q tflite-model-maker
!pip install --upgrade tensorboard
!pip install --upgrade tensorflow
!pip install tensorrt
!pip install protobuf==3.20 --upgrade
!pip install scann
