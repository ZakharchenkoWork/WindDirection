package com.example.mistery.winddirection;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ResultManager.*;

import com.example.mistery.winddirection.factorys.ChooserDialogInit;
import com.example.mistery.winddirection.factorys.PickerDialogInit;
import com.znshadows.dialogs.ChooserDialog;
import com.znshadows.dialogs.PickerDialog;


public class MainActivity extends AppCompatActivity {

    public static final String PREF_MODE_OF_SPEED = "mode of speed";
    public static final String PREF_MODE_OF_DIRECTION = "mode of direction";
    public static final String PREF_MODE_OF_LANGUAGE = "mode of language";

    public static int MODE_OF_SPEED;
    public static int MODE_OF_DIRECTION;
    public static int MODE_OF_LANGUAGE;

    static String[] directionDiplayTypes;
    static String[] speedDiplayTypes;
    static String[] languageDiplayTypes = new String[]{"English", "Русский", "Tagalog"};

    static Characteristics shipCharacteristics = new Characteristics();
    static Characteristics windCharacteristics = new Characteristics();

    Typeface projectTypeface = null;

    Button shipsCourse;
    Button shipsSpeed;
    Button windDirection;
    Button windSpeed;

    TextView ownShipLabel;
    TextView rWindLabel;
    TextView tWindLabel;
    TextView resultTextView;

    ImageView shipsCourseVectorImage;
    ImageView windDirectionVectorImage;
    ImageView resultVectorImage;
    ImageView windDirectionArrowImage;
    ImageView resultArrowImage;

    float scale = 5;
    ArrayList<Result> speedResult;
    ArrayList<Result> directionResult;
    Map<Integer, PickerDialogInit> pickerDialogCallMethods = new HashMap<Integer, PickerDialogInit>();
    Map<Integer, ChooserDialogInit> chooserDialogCallMethods = new HashMap<Integer, ChooserDialogInit>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        directionDiplayTypes = new String[]{getString(R.string.degs), getString(R.string.rumb), getString(R.string.rumbAccurate)};
        speedDiplayTypes = new String[]{getString(R.string.knots), getString(R.string.beaufort), getString(R.string.kph), getString(R.string.mps), getString(R.string.mph)};
        //languageDiplayTypes = new String[] {"...."}; Allready inserted

        prepareResultMethods();

        DirectionPickerDialog.setInnerResultUnits(getString(R.string.degrees));
        SpeedPickerDialog.setInnerResultUnits(getString(R.string.knots));


        loadPreferences();
        if (!getResources().getConfiguration().locale.equals(prepareLanguage(languageDiplayTypes[MODE_OF_LANGUAGE]))) {
            changeLanguage(languageDiplayTypes[MODE_OF_LANGUAGE]);
        }

        if (MODE_OF_LANGUAGE == 1) {
            projectTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Light.ttf");
        } else {
            projectTypeface = Typeface.createFromAsset(this.getAssets(), "fonts/Amsdam_Regular.ttf");
        }

        View.OnClickListener listener = prepareOnClickListener();
        initialiseViews(listener);
        initializeDialogs();
        initializeMenu();
        refreshGraphics();

    }

    private void initializeMenu() {
        chooserDialogCallMethods.put(R.id.direction, new ChooserDialogInit() {
            @Override
            public void init() {
                new ChooserDialog(MainActivity.this, getString(R.string.direction_display), directionDiplayTypes, new ChooserDialog.OnChooseListener() {
                    @Override
                    public void onChoose(int result) {
                        MODE_OF_DIRECTION = result;
                        refreshGraphics();
                        savePreferences();
                    }
                });
            }
        });
        chooserDialogCallMethods.put(R.id.speed, new ChooserDialogInit() {
            @Override
            public void init() {
                new ChooserDialog(MainActivity.this, getString(R.string.speed_display), speedDiplayTypes, new ChooserDialog.OnChooseListener() {
                    @Override
                    public void onChoose(int result) {

                        MODE_OF_SPEED = result;
                        refreshGraphics();
                        savePreferences();

                    }
                });
            }
        });
        chooserDialogCallMethods.put(R.id.language, new ChooserDialogInit() {
            @Override
            public void init() {
                new ChooserDialog(MainActivity.this, getString(R.string.language), languageDiplayTypes, new ChooserDialog.OnChooseListener() {
                    @Override
                    public void onChoose(int result) {
                        Log.i("Menu", "choosen " + result);
                        MODE_OF_LANGUAGE = result;
                        savePreferences();
                        restartActivity();
                    }
                });
            }
        });
    }

    private void initializeDialogs() {
        pickerDialogCallMethods.put(R.id.shipsCourse, new PickerDialogInit() {
            @Override
            public void init() {
                new DirectionPickerDialog(MainActivity.this, getString(R.string.setSCourse), shipCharacteristics.getCourse())
                        .setFont(projectTypeface)
                        .setOnDoneListener(new PickerDialog.OnDoneListener() {
                            @Override
                            public void onDone(float result) {
                                shipCharacteristics.setCourse(result);
                                refreshGraphics();
                            }
                        }).show();
            }
        });
        pickerDialogCallMethods.put(R.id.shipsSpeed, new PickerDialogInit() {
            @Override
            public void init() {
                new SpeedPickerDialog(MainActivity.this, getString(R.string.setSSpeed), shipCharacteristics.getSpeed())
                        .setFont(projectTypeface)
                        .setOnDoneListener(new PickerDialog.OnDoneListener() {
                            @Override
                            public void onDone(float result) {
                                shipCharacteristics.setSpeed(result);
                                refreshGraphics();
                            }
                        }).show();
            }
        });
        pickerDialogCallMethods.put(R.id.windDirection, new PickerDialogInit() {
            @Override
            public void init() {
                new DirectionPickerDialog(MainActivity.this, getString(R.string.setWCourse), windCharacteristics.getCourse())
                        .setFont(projectTypeface)
                        .setOnDoneListener(new PickerDialog.OnDoneListener() {
                            @Override
                            public void onDone(float result) {
                                windCharacteristics.setCourse(result);
                                refreshGraphics();
                            }
                        }).show();
            }
        });
        pickerDialogCallMethods.put(R.id.windSpeed, new PickerDialogInit() {
            @Override
            public void init() {
                new SpeedPickerDialog(MainActivity.this, getString(R.string.setWSpeed), windCharacteristics.getSpeed())
                        .setFont(projectTypeface)
                        .setOnDoneListener(new PickerDialog.OnDoneListener() {
                            @Override
                            public void onDone(float result) {
                                windCharacteristics.setSpeed(result);
                                refreshGraphics();
                            }
                        }).show();
            }
        });
    }

    /**
     * Loading and applying of previous settings of the app
     */
    private void loadPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        MODE_OF_SPEED = prefs.getInt(PREF_MODE_OF_SPEED, 0);
        MODE_OF_DIRECTION = prefs.getInt(PREF_MODE_OF_DIRECTION, 0);
        MODE_OF_LANGUAGE = prefs.getInt(PREF_MODE_OF_LANGUAGE, 0);
    }

    private void prepareResultMethods() {

        speedResult = new ArrayList<Result>();
        speedResult.add(new SpeedResultKnots());
        speedResult.add(new SpeedResultBeaufort());
        speedResult.add(new SpeedResultKPH());
        speedResult.add(new SpeedResultMPS());
        speedResult.add(new SpeedResultMPH());

        directionResult = new ArrayList<Result>();
        directionResult.add(new DirectionResultDegrees());
        directionResult.add(new DirectionResultRumbs());
        directionResult.add(new DirectionResultAcurateRumbs());
    }

    private void initialiseViews(View.OnClickListener listener) {
        shipsCourse = (Button) findViewById(R.id.shipsCourse);
        shipsSpeed = (Button) findViewById(R.id.shipsSpeed);
        windDirection = (Button) findViewById(R.id.windDirection);
        windSpeed = (Button) findViewById(R.id.windSpeed);

        resultTextView = (TextView) findViewById(R.id.result);
        ownShipLabel = (TextView) findViewById(R.id.ownShipLabel);
        rWindLabel = (TextView) findViewById(R.id.rWindLabel);
        tWindLabel = (TextView) findViewById(R.id.tWindLabel);

        shipsCourseVectorImage = (ImageView) findViewById(R.id.shipsCourseVector);

        windDirectionVectorImage = (ImageView) findViewById(R.id.windDirectionVector);
        windDirectionArrowImage = (ImageView) findViewById(R.id.windDirectionArrow);
        resultVectorImage = (ImageView) findViewById(R.id.resultVector);
        resultArrowImage = (ImageView) findViewById(R.id.resultArrow);

        shipsCourse.setOnClickListener(listener);
        shipsSpeed.setOnClickListener(listener);
        windDirection.setOnClickListener(listener);
        windSpeed.setOnClickListener(listener);

        configureFont(projectTypeface);
    }

    /**
     * Used for correct displaying of the vectors, prevents them from becoming too big and run out of screen
     * @param shipsSpeed
     * @param windSpeed
     * @param trueSpeed
     */
    private void refreshScale(float shipsSpeed, float windSpeed, float trueSpeed) {
        float biggest = 0;
        if (shipsSpeed < windSpeed) {
            biggest = windSpeed;
        } else {
            biggest = shipsSpeed;
        }

        if (biggest < trueSpeed) {
            biggest = trueSpeed;
        }
        //with speed 20 knots scale must be 5, every 20 knots, scale is increased by 5
        scale = (5 + (5 * (int) (biggest / 21)));

    }

    /**
     * Used for refreshing of all of the pictures of vectors at once
     */
    public void refreshGraphics() {
        //in case the final vector is ready for displaying
        if (shipCharacteristics.isReady() && windCharacteristics.isReady()) {
            prepareTotalTrueWindGraphics();
        } else {
            resetTrueWindGraphics();
        }
        // displaying of picture for the ships vector
        if (shipCharacteristics.isReady()) {
            prepareTotalShipGraphics();
        }

        if (shipCharacteristics.isCourseSet()) {
            prepareShipCourseGraphics();
        }

        if (shipCharacteristics.isSpeedSet()) {
            prepareShipSpeedGraphics();
        }

        // displaying of picture for the wind vector
        if (windCharacteristics.isReady()) {
            prepareTotalRelativeWindGraphics();
        }

        if (windCharacteristics.isCourseSet()) {
            prepareRelativeWindDirectionGraphics();
        }

        if (windCharacteristics.isSpeedSet()) {
            prepareRelativeWindSpeedGraphics();
        }


    }

    private void resetTrueWindGraphics() {
        refreshScale(shipCharacteristics.getSpeed(), windCharacteristics.getSpeed(), 0);
        resultVectorImage.setScaleY(1);
        resultVectorImage.setRotation(0);
    }

    private void prepareTotalTrueWindGraphics() {
        Characteristics trueWind = (Characteristics) Characteristics.calculateTrueWind(shipCharacteristics, windCharacteristics);
        resultTextView.setText(getResultString(trueWind, MODE_OF_DIRECTION, MODE_OF_SPEED));
        resultVectorImage.setBackgroundResource(R.drawable.result);
        resultArrowImage.setBackgroundResource(R.drawable.result_arrow);
        refreshScale(shipCharacteristics.getSpeed(), windCharacteristics.getSpeed(), trueWind.getSpeed());
        resultVectorImage.setScaleY(trueWind.getSpeed() / scale);
        resultVectorImage.setRotation(trueWind.getCourse());
        resultArrowImage.setRotation(trueWind.getCourse());

    }

    private void prepareTotalShipGraphics() {
        shipsCourseVectorImage.setBackgroundResource(R.drawable.course);
    }

    private void prepareRelativeWindSpeedGraphics() {
        windDirectionVectorImage.setScaleY(windCharacteristics.getSpeed() / scale);
        windSpeed.setText(getString(R.string.rwSpeed) + ": " + speedResult.get(0).getResult(windCharacteristics.getSpeed(), getString(R.string.shortForKnots)));
    }

    private void prepareRelativeWindDirectionGraphics() {
        windDirectionVectorImage.setRotation(windCharacteristics.getCourse());
        windDirectionArrowImage.setRotation(windCharacteristics.getCourse());
        windDirection.setText(getString(R.string.rwCourse) + ": " + directionResult.get(0).getResult(windCharacteristics.getCourse(), getString(R.string.degrees)));
    }

    private void prepareTotalRelativeWindGraphics() {
        windDirectionVectorImage.setBackgroundResource(R.drawable.direction);
        windDirectionArrowImage.setBackgroundResource(R.drawable.direction_arrow);
    }

    private void prepareShipSpeedGraphics() {
        shipsCourseVectorImage.setScaleY(shipCharacteristics.getSpeed() / scale);
        shipsSpeed.setText(getString(R.string.sSpeed) + ": " + speedResult.get(0).getResult(shipCharacteristics.getSpeed(), getString(R.string.shortForKnots)));
    }

    private void prepareShipCourseGraphics() {
        shipsCourseVectorImage.setRotation(shipCharacteristics.getCourse());
        shipsCourse.setText(getString(R.string.sCourse) + ": " + directionResult.get(0).getResult(shipCharacteristics.getCourse(), getString(R.string.degrees)));
    }

    public String getResultString(Characteristics trueWind, int modeOfDirection, int modeOfSpeed) {

        String resultString = getString(R.string.direction) + ": ";

        if (modeOfDirection == 0) {
            resultString += directionResult.get(modeOfDirection).getResult(trueWind.getCourse(), getString(R.string.degrees));
        } else {
            resultString += directionResult.get(modeOfDirection).getResult(trueWind.getCourse(), "");
        }

        resultString += ", " + getString(R.string.speed) + ": ";

        resultString += speedResult.get(modeOfSpeed).getResult(trueWind.getSpeed(), speedDiplayTypes[modeOfSpeed]);

        return resultString;

    }


    private View.OnClickListener prepareOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pickerDialogCallMethods.get(v.getId()).init();
            }
        };

        return listener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        chooserDialogCallMethods.get(item.getItemId()).init();
        return super.onOptionsItemSelected(item);
    }

    private void savePreferences() {
        PreferenceManager.getDefaultSharedPreferences(MainActivity.this)
                .edit()
                .putInt(PREF_MODE_OF_DIRECTION, MODE_OF_DIRECTION)
                .putInt(PREF_MODE_OF_SPEED, MODE_OF_SPEED)
                .putInt(PREF_MODE_OF_LANGUAGE, MODE_OF_LANGUAGE)
                .commit();


    }

    /**
     * Preparing Locale for required Language
     *
     * @param lang lang language to set
     * @return Locale for this language
     */
    Locale prepareLanguage(String lang) {
        Locale locale = null;

        if (lang.equals(languageDiplayTypes[0])) {
            locale = new Locale("en");
        }
        if (lang.equals(languageDiplayTypes[1])) {
            locale = new Locale("ru");

        }
        if (lang.equals(languageDiplayTypes[2])) {
            locale = new Locale("tl", "PH");
        }
        return locale;
    }

    /**
     * changes language of the app
     *
     * @param lang language to set
     */
    void changeLanguage(String lang) {

        Configuration conf = new Configuration();
        conf.locale = prepareLanguage(lang);
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        restartActivity();
    }

    /**
     * Refreshes Activity
     */
    void restartActivity() {
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    void configureFont(Typeface typeface) {
        shipsCourse.setTypeface(typeface);
        shipsSpeed.setTypeface(typeface);
        windDirection.setTypeface(typeface);
        windSpeed.setTypeface(typeface);
        resultTextView.setTypeface(typeface);
        ownShipLabel.setTypeface(typeface);
        rWindLabel.setTypeface(typeface);
        tWindLabel.setTypeface(typeface);
    }
}

