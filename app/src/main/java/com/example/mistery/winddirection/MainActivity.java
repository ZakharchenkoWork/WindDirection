package com.example.mistery.winddirection;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mistery.winddirection.factorys.ChooserDialogInit;
import com.example.mistery.winddirection.factorys.PickerDialogInit;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;
import com.znshadows.dialogs.ChooserDialog;
import com.znshadows.dialogs.PickerDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ResultManager.DirectionResultAcurateRumbs;
import ResultManager.DirectionResultDegrees;
import ResultManager.DirectionResultRumbs;
import ResultManager.Result;
import ResultManager.SpeedResultBeaufort;
import ResultManager.SpeedResultKPH;
import ResultManager.SpeedResultKnots;
import ResultManager.SpeedResultMPH;
import ResultManager.SpeedResultMPS;


public class MainActivity extends AppCompatActivity {

    public static final String PREF_MODE_OF_SPEED = "mode of speed";
    public static final String PREF_MODE_OF_DIRECTION = "mode of direction";
    public static final String PREF_MODE_OF_LANGUAGE = "mode of language";

    public static int MODE_OF_SPEED;
    public static int MODE_OF_DIRECTION;
    public static int MODE_OF_LANGUAGE;


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
    ShimmerTextView resultTextView;

    ImageView shipsCourseVectorImage;
    ImageView windDirectionVectorImage;
    ImageView resultVectorImage;
    ImageView windDirectionArrowImage;
    ImageView resultArrowImage;

    ResideMenu resideMenu;
    CustomizableResideMenuItem direction;
    CustomizableResideMenuItem distance;
    CustomizableResideMenuItem language;
    float scale = 5;
    ArrayList<Result> speedResult;
    ArrayList<Result> directionResult;
    Map<Integer, PickerDialogInit> pickerDialogCallMethods = new HashMap<Integer, PickerDialogInit>();
    Map<View, ChooserDialogInit> chooserDialogCallMethods = new HashMap<View, ChooserDialogInit>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /*Intent intent = new Intent(this, Test.class);
        startActivity(intent);*/


        prepareSpeedResultMethods();
        prepareDirectionResultMethods();
        DirectionPickerDialog.setInnerResultUnits(getString(R.string.degrees));
        SpeedPickerDialog.setInnerResultUnits(getString(R.string.knots));


        loadPreferences();
        if (!getResources().getConfiguration().locale.equals(prepareLanguage(getResources().getStringArray(R.array.languageDiplayTypes)[MODE_OF_LANGUAGE]))) {
            changeLanguage(getResources().getStringArray(R.array.languageDiplayTypes)[MODE_OF_LANGUAGE]);
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
        configureMenu();


    }

    private void initializeMenu() {
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);

        // create menu items;
        String titles[] = {getString(R.string.direction_display), getString(R.string.speed_display), getString(R.string.language)};
        int icon[] = {R.drawable.menu_deg, R.drawable.menu_dist, R.drawable.menu_lang};
        View.OnClickListener menuListener = prepareMenuListener();


        direction = new CustomizableResideMenuItem(this, icon[0], titles[0]);
        direction.setOnClickListener(menuListener);
        resideMenu.addMenuItem(direction, ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        distance = new CustomizableResideMenuItem(this, icon[1], titles[1]);
        distance.setOnClickListener(menuListener);
        resideMenu.addMenuItem(distance, ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        language = new CustomizableResideMenuItem(this, icon[2], titles[2]);
        language.setOnClickListener(menuListener);
        resideMenu.addMenuItem(language, ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        prepareMenuActions(direction, distance, language);
    }

    private void prepareMenuActions(View direction, View distance, View language) {
        chooserDialogCallMethods.put(direction, new ChooserDialogInit() {
            @Override
            public void init() {
                new ChooserDialog(MainActivity.this, getString(R.string.direction_display), getResources().getStringArray(R.array.directionDiplayTypes), new ChooserDialog.OnChooseListener() {
                    @Override
                    public void onChoose(int result) {
                        MODE_OF_DIRECTION = result;
                        refreshGraphics();
                        savePreferences();
                    }
                });
            }
        });
        chooserDialogCallMethods.put(distance, new ChooserDialogInit() {
            @Override
            public void init() {
                new ChooserDialog(MainActivity.this, getString(R.string.speed_display), getResources().getStringArray(R.array.speedDiplayTypes), new ChooserDialog.OnChooseListener() {
                    @Override
                    public void onChoose(int result) {

                        MODE_OF_SPEED = result;
                        refreshGraphics();
                        savePreferences();

                    }
                });
            }
        });
        chooserDialogCallMethods.put(language, new ChooserDialogInit() {
            @Override
            public void init() {
                new ChooserDialog(MainActivity.this, getString(R.string.language), getResources().getStringArray(R.array.languageDiplayTypes), new ChooserDialog.OnChooseListener() {
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
        pickerDialogCallMethods.put(R.id.shipsCourseButton, new PickerDialogInit() {
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
        pickerDialogCallMethods.put(R.id.shipsSpeedButton, new PickerDialogInit() {
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
        pickerDialogCallMethods.put(R.id.relativeWindDirectionButton, new PickerDialogInit() {
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
        pickerDialogCallMethods.put(R.id.relativeWindSpeedButton, new PickerDialogInit() {
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
     * Loading and applying previous settings of the app
     */
    private void loadPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        MODE_OF_SPEED = prefs.getInt(PREF_MODE_OF_SPEED, 0);
        MODE_OF_DIRECTION = prefs.getInt(PREF_MODE_OF_DIRECTION, 0);
        MODE_OF_LANGUAGE = prefs.getInt(PREF_MODE_OF_LANGUAGE, 0);
    }

    /**
     * Handles the opening and closing of the resideMenu
     *
     * @param ev MotionEvent
     * @return MotionEvent, back to system
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    /**
     * prepares display units for speed,
     * for easier access according to settings
     */
    private void prepareSpeedResultMethods() {
        //correct displaying modes will be accessible from this array
        speedResult = new ArrayList<Result>();
        speedResult.add(new SpeedResultKnots());
        speedResult.add(new SpeedResultBeaufort());
        speedResult.add(new SpeedResultKPH());
        speedResult.add(new SpeedResultMPS());
        speedResult.add(new SpeedResultMPH());
    }


    /**
     * prepares display units for Direction
     * for easier access according to settings
     */
    private void prepareDirectionResultMethods() {
        //correct displaying modes will be accessible from this array
        directionResult = new ArrayList<Result>();
        directionResult.add(new DirectionResultDegrees());
        directionResult.add(new DirectionResultRumbs());
        directionResult.add(new DirectionResultAcurateRumbs());
    }

    /**
     * all GUI initialization will go here
     *
     * @param listener click listener for buttons
     */
    private void initialiseViews(View.OnClickListener listener) {

        shipsCourse = (Button) findViewById(R.id.shipsCourseButton);
        shipsSpeed = (Button) findViewById(R.id.shipsSpeedButton);
        windDirection = (Button) findViewById(R.id.relativeWindDirectionButton);
        windSpeed = (Button) findViewById(R.id.relativeWindSpeedButton);

        resultTextView = (ShimmerTextView) findViewById(R.id.result);
        Shimmer shimerAnimation = new Shimmer();
        shimerAnimation.setDuration(1500);
        shimerAnimation.start(resultTextView);

        ownShipLabel = (TextView) findViewById(R.id.ownShipLabel);
        rWindLabel = (TextView) findViewById(R.id.relativeWindLabel);
        tWindLabel = (TextView) findViewById(R.id.trueWindLabel);

        shipsCourseVectorImage = (ImageView) findViewById(R.id.shipsCourseVector);
        windDirectionVectorImage = (ImageView) findViewById(R.id.windDirectionVector);
        windDirectionArrowImage = (ImageView) findViewById(R.id.windDirectionArrow);
        resultVectorImage = (ImageView) findViewById(R.id.resultVector);
        resultArrowImage = (ImageView) findViewById(R.id.resultArrow);

        shipsCourse.setOnClickListener(listener);
        shipsSpeed.setOnClickListener(listener);
        windDirection.setOnClickListener(listener);
        windSpeed.setOnClickListener(listener);


    }

    /**
     * Used for correct displaying of the vectors, prevents them from becoming too big and run out of screen
     *
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
        if (!shipCharacteristics.isReady() && !windCharacteristics.isReady()) {
            resetTrueWindGraphics();
        } else {
            prepareTotalTrueWindGraphics();
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

    /**
     * resets pictures for true wind, must be used at the start of the app
     */
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
        TextView relativeWindSpeedLabel = (TextView) findViewById(R.id.relativeWindSpeedLabel);
        relativeWindSpeedLabel.setText(speedResult.get(0).getResult(windCharacteristics.getSpeed(), getString(R.string.shortForKnots)));
    }

    private void prepareRelativeWindDirectionGraphics() {
        windDirectionVectorImage.setRotation(windCharacteristics.getCourse());
        windDirectionArrowImage.setRotation(windCharacteristics.getCourse());
        TextView relativeWindDirectionLabel = (TextView) findViewById(R.id.relativeWindDirectionLabel);
        relativeWindDirectionLabel.setText(directionResult.get(0).getResult(windCharacteristics.getCourse(), getString(R.string.degrees)));
    }

    private void prepareTotalRelativeWindGraphics() {
        windDirectionVectorImage.setBackgroundResource(R.drawable.direction);
        windDirectionArrowImage.setBackgroundResource(R.drawable.direction_arrow);
    }

    private void prepareShipSpeedGraphics() {
        shipsCourseVectorImage.setScaleY(shipCharacteristics.getSpeed() / scale);
        TextView shipsSpeedLabel = (TextView) findViewById(R.id.shipsSpeedLabel);
        shipsSpeedLabel.setText(speedResult.get(0).getResult(shipCharacteristics.getSpeed(), getString(R.string.shortForKnots)));
    }

    private void prepareShipCourseGraphics() {
        shipsCourseVectorImage.setRotation(shipCharacteristics.getCourse());
        TextView shipsCourseLabel = (TextView) findViewById(R.id.shipsCourseLabel);
        shipsCourseLabel.setText(directionResult.get(0).getResult(shipCharacteristics.getCourse(), getString(R.string.degrees)));
    }

    public String getResultString(Characteristics trueWind, int modeOfDirection, int modeOfSpeed) {

        String resultString = getString(R.string.direction) + ": ";

        if (modeOfDirection == 0) {
            resultString += directionResult.get(modeOfDirection).getResult(trueWind.getCourse(), getString(R.string.degrees));
        } else {
            resultString += directionResult.get(modeOfDirection).getResult(trueWind.getCourse(), "");
        }

        resultString += ", " + getString(R.string.speed) + ": ";

        resultString += speedResult.get(modeOfSpeed).getResult(trueWind.getSpeed(), getResources().getStringArray(R.array.speedDiplayTypes)[modeOfSpeed]);

        return resultString;

    }


    private View.OnClickListener prepareOnClickListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("listener", "" + v.getId());
                pickerDialogCallMethods.get(v.getId()).init();
            }
        };

        return listener;
    }

    private View.OnClickListener prepareMenuListener() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("listener", "" + v.getId());
                chooserDialogCallMethods.get(v).init();
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

        if (lang.equals(getResources().getStringArray(R.array.languageDiplayTypes)[0])) {
            locale = new Locale("en");
        }
        if (lang.equals(getResources().getStringArray(R.array.languageDiplayTypes)[1])) {
            locale = new Locale("ru");

        }
        if (lang.equals(getResources().getStringArray(R.array.languageDiplayTypes)[2])) {
            locale = new Locale("tl", "PH");
        }
        return locale;
    }

    /**
     * changes language of the app
     *
     * @param lang language to set
     */
    private void changeLanguage(String lang) {

        Configuration conf = new Configuration();
        conf.locale = prepareLanguage(lang);
        getBaseContext().getResources().updateConfiguration(conf, getBaseContext().getResources().getDisplayMetrics());
        restartActivity();
    }

    /**
     * Refreshes Activity , use in case of changes of the apps settings
     */
    private void restartActivity() {
        startActivity(new Intent(MainActivity.this, MainActivity.class));
        finish();
    }

    /**
     * Use to configure Menu items style
     */
    private void configureMenu() {
        configureMenuTextView(direction.getTextView());
        configureMenuTextView(distance.getTextView());
        configureMenuTextView(language.getTextView());
    }

    /**
     * method used to bring Menu Items to the same style
     * @param tv
     */
    private void configureMenuTextView(TextView tv){

        if (tv == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < 23) {
            tv.setTextAppearance(this, R.style.AppTheme);
            tv.setTextColor(getResources().getColor(R.color.text_color));
        } else {
            tv.setTextColor(getColor(R.color.text_color));
            tv.setTextAppearance(R.style.AppTheme);
        }

        tv.setMaxLines(1);
        tv.setTextSize(getResources().getDimension(R.dimen.text_size_normal) / getResources().getDisplayMetrics().density);
    }
}

