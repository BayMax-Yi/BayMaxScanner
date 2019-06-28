# CodeScanner

<img height=600 width=400 src="https://github.com/BayMax-Yi/BayMaxScanner/blob/master/screenshort/scan_qr.gif" frameborder=0 allowfullscreen></img>

<img height=600 width=400 src="https://github.com/BayMax-Yi/BayMaxScanner/blob/master/screenshort/scan_barcode.GIF" frameborder=0 allowfullscreen></img>

<img height=600 width=400 src="https://github.com/BayMax-Yi/BayMaxScanner/blob/master/screenshort/create_qr.gif" frameborder=0 allowfullscreen></img>

<img height=600 width=400 src="https://github.com/BayMax-Yi/BayMaxScanner/blob/master/screenshort/create_barcode.gif" frameborder=0 allowfullscreen></img>

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
	        implementation 'com.github.BayMax-Yi:BayMaxScanner:1.0.1'
}


### Step 3. Init PreViewHelper  in your CaptureActivity


     @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        
        previewHelper = new PreviewHelper();
	 previewHelper.setScanType( PreviewHelper.SCANTYPE_QR);
        previewHelper.setActivity(this);
        previewHelper.setSurfaceHolder(surfaceView.getHolder());
        previewHelper.setViewfinderView(viewfinderView);
        //Set onDecodeListener for PreviewHelper and deal the decoded result in onDecodeListener.onDecodedResult(Result) 
        previewHelper.setOnDecodedResultListener(onDecodeListener);
        
        //setScanType   PreviewHelper.SCANTYPE_QR ： Scan QrCode     
        //              PreviewHelper.SCANTYPE_BARCIDE ： Scan BarCode  
        
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
    
