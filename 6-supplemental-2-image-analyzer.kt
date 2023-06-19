@androidx.camera.core.ExperimentalGetImage
class ImageAnalyzer(
    private val imageLabeler: ImageLabeler,
    private val onImageAnalyzerOutputs: (List<ImageLabel>) -> Unit,
) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)

            imageLabeler.process(image)
                .addOnSuccessListener { labels ->
                    onImageAnalyzerOutputs(labels)
                    imageProxy.close()
                }
        }
    }
}
