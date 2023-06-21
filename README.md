# kcdc-ml-kit

[Slides](https://docs.google.com/presentation/d/1FdFz1HUA0wu3TcfKxcP6FRFK0XoE1I_gUUPqTQveyxU/edit?usp=sharing) 


Dependencies 

Part 1  
```
// Image labelling default   
implementation 'com.google.mlkit:image-labeling:17.0.7'
```

Part 2 
```
// Image labelling custom   
implementation 'com.google.mlkit:image-labeling-custom:17.0.1'
```

Part 3  
```
// CameraX Lifecycle library    
implementation "androidx.camera:camera-lifecycle:1.2.3"    
implementation "androidx.camera:camera-camera2:1.2.3"       
implementation "androidx.camera:camera-view:1.2.3"     
```

Permissions    
```
<uses-feature android:name="android.hardware.camera.any" />     
<uses-permission android:name="android.permission.CAMERA" />  
```
