package com.uni.julio.supertvplus.view;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.uni.julio.supertvplus.R;
import com.uni.julio.supertvplus.service.test.GetSpeedTestHostsHandler;
import com.uni.julio.supertvplus.service.test.HttpDownloadTest;
import com.uni.julio.supertvplus.service.test.HttpUploadTest;
import com.uni.julio.supertvplus.service.test.PingTest;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


public class SpeedTestActivity extends AppCompatActivity {
    static int position = 0;
    static int lastPosition = 0;
    GetSpeedTestHostsHandler getSpeedTestHostsHandler = null;
    HashSet<String> tempBlackList;
    HashMap<Integer, String> mapKey;
    HashMap<Integer, List<String>> mapValue;
    int findServerIndex = 0;
    TextView startButton;
    TextView severSelectButton;
    boolean serverLoaded = false;
    @Override
    public void onResume() {
        super.onResume();

        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
        getSpeedTestHostsHandler.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedtest1);

        startButton =  findViewById(R.id.startButton);
        severSelectButton =  findViewById(R.id.selectServer);
        severSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mapValue != null && mapValue.size() > 0) {
                    try{
                        Intent launchIntent = new Intent(getBaseContext(),SelectServerActivity.class);
                        launchIntent.putExtra("SERVERS", mapValue);
                        startActivityForResult(launchIntent,100);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }else{
                    serverLoaded = false;
                    startButton.setEnabled(true);
                    severSelectButton.setEnabled(true);
                    startButton.setText("Load Servers");
                    severSelectButton.setText("Servers are not laoded");
                }
            }
        });
        final DecimalFormat dec = new DecimalFormat("#.##");
        tempBlackList = new HashSet<>();
        try{
            getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
            getSpeedTestHostsHandler.start();

            startButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startButton.setText("Loading...");
                    //Restart test icin eger baglanti koparsa
                    if (getSpeedTestHostsHandler == null) {
                        getSpeedTestHostsHandler = new GetSpeedTestHostsHandler();
                        getSpeedTestHostsHandler.start();
                    }

                    new Thread(new Runnable() {
                        RotateAnimation rotate;
                        ImageView barImageView =  findViewById(R.id.barImageView);
                        TextView pingTextView =  findViewById(R.id.pingTextView);
                        TextView downloadTextView =  findViewById(R.id.downloadTextView);
                        TextView uploadTextView =  findViewById(R.id.uploadTextView);

                        @Override
                        public void run() {
                            //Get egcodes.speedtest hosts
                            int timeCount = 600; //1min
                            while (getSpeedTestHostsHandler != null && !getSpeedTestHostsHandler.isFinished()) {
                                timeCount--;
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                }
                                if (timeCount <= 0) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "No Connection...", Toast.LENGTH_LONG).show();
                                            startButton.setEnabled(true);
                                            severSelectButton.setEnabled(true);
                                            startButton.setText("Restart Test");
                                        }
                                    });
                                    getSpeedTestHostsHandler = null;
                                    return;
                                }
                            }

                            if(getSpeedTestHostsHandler == null) return;
                            mapKey = getSpeedTestHostsHandler.getMapKey();
                            mapValue = getSpeedTestHostsHandler.getMapValue();
                            //Find closest server
                            String uploadAddr = mapKey.get(findServerIndex);
                            final List<String> info = mapValue.get(findServerIndex);
                            if(!serverLoaded){
                                showDetail();
                                return;
                            }
                            if (info == null) {
                                return;
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startButton.setEnabled(false);
                                    severSelectButton.setEnabled(false);
                                }
                            });

                            //Init Ping graphic
                            //final LinearLayout chartPing = (LinearLayout) findViewById(R.id.chartPing);
                            /*XYSeriesRenderer pingRenderer = new XYSeriesRenderer();
                            XYSeriesRenderer.FillOutsideLine pingFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                            pingFill.setColor(Color.parseColor("#4d5a6a"));
                            pingRenderer.addFillOutsideLine(pingFill);
                            pingRenderer.setDisplayChartValues(false);
                            pingRenderer.setShowLegendItem(false);
                            pingRenderer.setColor(Color.parseColor("#4d5a6a"));
                            pingRenderer.setLineWidth(5);
                            final XYMultipleSeriesRenderer multiPingRenderer = new XYMultipleSeriesRenderer();
                            multiPingRenderer.setXLabels(0);
                            multiPingRenderer.setYLabels(0);
                            multiPingRenderer.setZoomEnabled(false);
                            multiPingRenderer.setXAxisColor(Color.parseColor("#647488"));
                            multiPingRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                            multiPingRenderer.setPanEnabled(true, true);
                            multiPingRenderer.setZoomButtonsVisible(false);
                            multiPingRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                            multiPingRenderer.addSeriesRenderer(pingRenderer);

                            //Init Download graphic
                            //final LinearLayout chartDownload = (LinearLayout) findViewById(R.id.chartDownload);
                            XYSeriesRenderer downloadRenderer = new XYSeriesRenderer();
                            XYSeriesRenderer.FillOutsideLine downloadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                            downloadFill.setColor(Color.parseColor("#4d5a6a"));
                            downloadRenderer.addFillOutsideLine(downloadFill);
                            downloadRenderer.setDisplayChartValues(false);
                            downloadRenderer.setColor(Color.parseColor("#4d5a6a"));
                            downloadRenderer.setShowLegendItem(false);
                            downloadRenderer.setLineWidth(5);
                            final XYMultipleSeriesRenderer multiDownloadRenderer = new XYMultipleSeriesRenderer();
                            multiDownloadRenderer.setXLabels(0);
                            multiDownloadRenderer.setYLabels(0);
                            multiDownloadRenderer.setZoomEnabled(false);
                            multiDownloadRenderer.setXAxisColor(Color.parseColor("#647488"));
                            multiDownloadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                            multiDownloadRenderer.setPanEnabled(false, false);
                            multiDownloadRenderer.setZoomButtonsVisible(false);
                            multiDownloadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                            multiDownloadRenderer.addSeriesRenderer(downloadRenderer);

                            //Init Upload graphic
                            //final LinearLayout chartUpload = (LinearLayout) findViewById(R.id.chartUpload);
                            XYSeriesRenderer uploadRenderer = new XYSeriesRenderer();
                            XYSeriesRenderer.FillOutsideLine uploadFill = new XYSeriesRenderer.FillOutsideLine(XYSeriesRenderer.FillOutsideLine.Type.BOUNDS_ALL);
                            uploadFill.setColor(Color.parseColor("#4d5a6a"));
                            uploadRenderer.addFillOutsideLine(uploadFill);
                            uploadRenderer.setDisplayChartValues(false);
                            uploadRenderer.setColor(Color.parseColor("#4d5a6a"));
                            uploadRenderer.setShowLegendItem(false);
                            uploadRenderer.setLineWidth(5);
                            final XYMultipleSeriesRenderer multiUploadRenderer = new XYMultipleSeriesRenderer();
                            multiUploadRenderer.setXLabels(0);
                            multiUploadRenderer.setYLabels(0);
                            multiUploadRenderer.setZoomEnabled(false);
                            multiUploadRenderer.setXAxisColor(Color.parseColor("#647488"));
                            multiUploadRenderer.setYAxisColor(Color.parseColor("#2F3C4C"));
                            multiUploadRenderer.setPanEnabled(false, false);
                            multiUploadRenderer.setZoomButtonsVisible(false);
                            multiUploadRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));
                            multiUploadRenderer.addSeriesRenderer(uploadRenderer);*/

                            //Reset value, graphics
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pingTextView.setText("0 ms");
                                    //chartPing.removeAllViews();
                                    downloadTextView.setText("0 Mbps");
                                    //chartDownload.removeAllViews();
                                    uploadTextView.setText("0 Mbps");
                                    // chartUpload.removeAllViews();
                                }
                            });
                            final List<Double> pingRateList = new ArrayList<>();
                            final List<Double> downloadRateList = new ArrayList<>();
                            final List<Double> uploadRateList = new ArrayList<>();
                            boolean pingTestStarted = false;
                            boolean pingTestFinished = false;
                            boolean downloadTestStarted = false;
                            boolean downloadTestFinished = false;
                            boolean uploadTestStarted = false;
                            boolean uploadTestFinished = false;

                            //Init Test
                            final PingTest pingTest = new PingTest(info.get(6).replace(":8080", ""), 6);
                            final HttpDownloadTest downloadTest = new HttpDownloadTest(Objects.requireNonNull(uploadAddr).replace(uploadAddr.split("/")[uploadAddr.split("/").length - 1], ""));
                            final HttpUploadTest uploadTest = new HttpUploadTest(uploadAddr);


                            //Tests
                            while (true) {
                                if (!pingTestStarted) {
                                    pingTest.start();
                                    pingTestStarted = true;
                                }
                                if (pingTestFinished && !downloadTestStarted) {
                                    downloadTest.start();
                                    downloadTestStarted = true;
                                }
                                if (downloadTestFinished && !uploadTestStarted) {
                                    uploadTest.start();
                                    uploadTestStarted = true;
                                }


                                //Ping Test
                                if (pingTestFinished) {
                                    //Failure
                                    if (pingTest.getAvgRtt() == 0) {
                                        System.out.println("Ping error...");
                                    } else {
                                        //Success
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                pingTextView.setText(dec.format(pingTest.getAvgRtt()) + " ms");
                                            }
                                        });
                                    }
                                } else {
                                    pingRateList.add(pingTest.getInstantRtt());

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            pingTextView.setText(dec.format(pingTest.getInstantRtt()) + " ms");
                                        }
                                    });

                                }


                                //Download Test
                                if (pingTestFinished) {
                                    if (downloadTestFinished) {
                                        //Failure
                                        if (downloadTest.getFinalDownloadRate() == 0) {
                                            System.out.println("Download error...");
                                        } else {
                                            //Success
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    downloadTextView.setText(dec.format(downloadTest.getFinalDownloadRate()) + " Mbps");
                                                }
                                            });
                                        }
                                    } else {
                                        //Calc position
                                        double downloadRate = downloadTest.getInstantDownloadRate();
                                        downloadRateList.add(downloadRate);
                                        position = getPositionByRate(downloadRate);

                                        runOnUiThread(new Runnable() {

                                            @Override
                                            public void run() {
                                                rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                                rotate.setInterpolator(new LinearInterpolator());
                                                rotate.setDuration(100);
                                                barImageView.startAnimation(rotate);
                                                downloadTextView.setText(dec.format(downloadTest.getInstantDownloadRate()) + " Mbps");

                                            }

                                        });
                                        lastPosition = position;

                                    }
                                }


                                //Upload Test
                                if (downloadTestFinished) {
                                    if (uploadTestFinished) {
                                        //Failure
                                        if (uploadTest.getFinalUploadRate() == 0) {
                                            System.out.println("Upload error...");
                                        } else {
                                            //Success
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    uploadTextView.setText(dec.format(uploadTest.getFinalUploadRate()) + " Mbps");
                                                }
                                            });
                                        }
                                    } else {
                                        //Calc position
                                        double uploadRate = uploadTest.getInstantUploadRate();
                                        uploadRateList.add(uploadRate);
                                        position = getPositionByRate(uploadRate);
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                rotate = new RotateAnimation(lastPosition, position, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                                                rotate.setInterpolator(new LinearInterpolator());
                                                rotate.setDuration(100);
                                                barImageView.startAnimation(rotate);
                                                uploadTextView.setText(dec.format(uploadTest.getInstantUploadRate()) + " Mbps");
                                            }

                                        });
                                        lastPosition = position;


                                    }
                                }

                                //Test bitti
                                if (pingTestFinished && downloadTestFinished && uploadTest.isFinished()) {
                                    break;
                                }

                                if (pingTest.isFinished()) {
                                    pingTestFinished = true;
                                }
                                if (downloadTest.isFinished()) {
                                    downloadTestFinished = true;
                                }
                                if (uploadTest.isFinished()) {
                                    uploadTestFinished = true;
                                }

                                if (pingTestStarted && !pingTestFinished) {
                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        Thread.sleep(100);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            //Thread bitiminde button yeniden aktif ediliyor
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    startButton.setEnabled(true);
                                    severSelectButton.setEnabled(true);
                                    startButton.setTextSize(16);
                                    startButton.setText("Restart Test");
                                }
                            });


                        }
                    }).start();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Unknown Error", Toast.LENGTH_LONG).show();
            finish();
        }

    }
    public void showDetail(){
        double selfLat = GetSpeedTestHostsHandler.selfLat;
        double selfLon = GetSpeedTestHostsHandler.selfLon;
        Location source = new Location("Source");
        source.setLatitude(selfLat);
        source.setLongitude(selfLon);
        List<String> ls = GetSpeedTestHostsHandler.mapValue.get(findServerIndex);
        Location dest = new Location("Dest");
        if(ls == null ||  ls.size() < 1) return;
        dest.setLatitude(Double.parseDouble(ls.get(0)));
        dest.setLongitude(Double.parseDouble(ls.get(1)));
        final List<String> info = GetSpeedTestHostsHandler.mapValue.get(findServerIndex);
        final double distance = source.distanceTo(dest);
        if (info == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    severSelectButton.setText("There was a problem in getting Host Location. Try again later.");
                }
            });
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                serverLoaded = true;
                startButton.setText("Start Test");
                severSelectButton.setText(String.format("Host Location: %s [Distance: %s km]", info.get(2), new DecimalFormat("#.##").format(distance / 1000)));
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            if(data != null)
            {
                findServerIndex = data.getIntExtra("serverIndex",0);
                showDetail();
            }

        }
    }
    public int getPositionByRate(double rate) {
        if (rate <= 1) {
            return (int) (rate * 30);

        } else if (rate <= 10) {
            return (int) (rate * 6) + 30;

        } else if (rate <= 30) {
            return (int) ((rate - 10) * 3) + 90;

        } else if (rate <= 50) {
            return (int) ((rate - 30) * 1.5) + 150;

        } else if (rate <= 100) {
            return (int) ((rate - 50) * 1.2) + 180;
        }

        return 0;
    }
}