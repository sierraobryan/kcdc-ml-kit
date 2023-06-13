/** Request permissions prior to launching exercise.**/
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        if (result.all { it.value }) {
            Log.d("MainActivity", "All required permissions granted")
        }
    }

    LaunchedEffect(Unit) {
        launch {
            permissionLauncher.launch(
                arrayOf(Manifest.permission.CAMERA)
            )
        }
    }
