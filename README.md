# CodeScanner



## How to use ?

### Step 1. Add the JitPack repository to your project build.gradle

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}


### Step 2. Add the dependency to your model build.gradle

dependencies {
	        implementation 'com.github.BayMax-Yi:CodeScanner:-SNAPSHOT'
}


### Step 3. Init PreViewHelper  in your CaptureActivity


     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        
        previewHelper = new PreviewHelper();
        previewHelper.setActivity(this);
        previewHelper.setSurfaceHolder(surfaceView.getHolder());
        previewHelper.setViewfinderView(viewfinderView);
        //Set onDecodeListener for PreviewHelper and deal the decoded result in onDecodeListener.onDecodedResult(Result) 
        previewHelper.setOnDecodedResultListener(onDecodeListener);
        
        //setScanType   PreviewHelper.SCANTYPE_QR ： Scan QrCode     
        //              PreviewHelper.SCANTYPE_BARCIDE ： Scan BarCode  
        
        previewHelper.setScanType( PreviewHelper.SCANTYPE_QR);
        previewHelper.onCreate();
    }
    
    
   ### Step 4. Add lifecycle  in your CaptureActivity.
    
      @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ......
        previewHelper.onCreate();
    }
    
     @Override
    protected void onResume() {
        super.onResume();
        previewHelper.onResume();
    }
    
     @Override
    protected void onPause() {
        super.onPause();
        previewHelper.onPause();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        previewHelper.onDestroy();
    }
    
