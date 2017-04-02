package com.hoangsong.zumechat.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoangsong.zumechat.R;
import com.hoangsong.zumechat.connection.DownloadAsyncTask;
import com.hoangsong.zumechat.dialog.DialogAccountSuspended;
import com.hoangsong.zumechat.dialog.DialogChooseImage;
import com.hoangsong.zumechat.dialog.DialogInvalidToken;
import com.hoangsong.zumechat.helpers.Prefs;
import com.hoangsong.zumechat.models.AccountInfo;
import com.hoangsong.zumechat.models.Response;
import com.hoangsong.zumechat.untils.Constants;
import com.hoangsong.zumechat.untils.JsonCallback;
import com.hoangsong.zumechat.untils.PopupCallback;
import com.hoangsong.zumechat.untils.Utils;
import com.hoangsong.zumechat.view.CircleTransform;
import com.mukesh.countrypicker.fragments.CountryPicker;
import com.mukesh.countrypicker.interfaces.CountryPickerListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyProfileEditActivity extends AppCompatActivity implements View.OnClickListener, PopupCallback, JsonCallback {
    public static final int ID_CHOOSE_FORM_LIBRARY = 1;
    public static final int ID_TAKE_PHOTO = 2;
    private boolean chooseProfile = false;
    private boolean chooseBackground = false;
    private Context context;
    private EditText txtNickName, txtStatus, txtGender, txtCountry;
    private TextView tvCancel, tvSave, tvTitle;
    private ImageButton ibChooseAvatar, ibChooseBackground, ibtnBack;
    private ImageView ivAvatar, ivBackground;
    private String countryCode = "";
    private String pictureImagePath = "";
    private File imgFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile_edit);
        initUI();
    }

    private void initUI() {
        context = getApplicationContext();

        txtNickName = (EditText) findViewById(R.id.txtNickName);
        txtStatus = (EditText) findViewById(R.id.txtStatus);
        txtGender = (EditText) findViewById(R.id.txtGender);
        txtCountry = (EditText) findViewById(R.id.txtCountry);
        tvCancel = (TextView) findViewById(R.id.tvCancel);
        tvSave = (TextView) findViewById(R.id.tvSave);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        ibChooseAvatar = (ImageButton) findViewById(R.id.ibChooseAvatar);
        ibChooseBackground = (ImageButton) findViewById(R.id.ibChooseBackground);
        ibtnBack = (ImageButton) findViewById(R.id.ibtnBack);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        ivBackground = (ImageView) findViewById(R.id.ivBackground);

        ibChooseAvatar.setOnClickListener(this);
        ibChooseBackground.setOnClickListener(this);
        ibtnBack.setOnClickListener(this);
        txtGender.setOnClickListener(this);
        txtCountry.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);

        loadData();

    }

    private void loadData() {
        AccountInfo accountInfo = Prefs.getUserInfo();
        if (accountInfo != null) {
            tvTitle.setText(getString(R.string.app_name));
            txtNickName.setText(accountInfo.getUsername());
            txtStatus.setText(accountInfo.getDescription());
            txtGender.setText(accountInfo.getGender());
            txtCountry.setText(accountInfo.getCountry());
            countryCode = accountInfo.getCountry_code();
            String urlAvatar = accountInfo.getProfile_url();
            String urlBackground = accountInfo.getBackground_url();
            if (urlAvatar.equals("")) {
                Picasso.with(context).load(R.drawable.ic_profile_normal).fit().centerCrop().transform(new CircleTransform()).into(ivAvatar);
            } else {
                Picasso.with(context).load(urlAvatar).fit().centerCrop().transform(new CircleTransform()).into(ivAvatar);
            }
            if (!urlBackground.equals("")) {
                Picasso.with(context).load(urlBackground).fit().centerCrop().into(ivBackground);
            }
            countryCode = accountInfo.getCountry_code();
        }
    }

    private Bitmap checkImageBackground(Bitmap image) {
        if (image.getWidth() < 640) {
            Utils.showSimpleDialogAlert(this, getString(R.string.msg_error_choose_a_larger_image_size));
            return null;
        } else if (image.getHeight() < 410) {
            Utils.showSimpleDialogAlert(this, getString(R.string.msg_error_choose_a_larger_image_size));
            return null;
        } else if (image.getWidth() > 1242 && image.getHeight() > 615) {
            return Utils.scaleCenterCrop(image, 615, 1242);
        }
        return image;
    }

    private Bitmap checkImageProfilePhoto(Bitmap image) {
        if (image.getWidth() < 240) {
            Utils.showSimpleDialogAlert(this, getString(R.string.msg_error_choose_a_larger_image_size));
            return null;
        } else if (image.getHeight() < 240) {
            Utils.showSimpleDialogAlert(this, getString(R.string.msg_error_choose_a_larger_image_size));
            return null;
        } else if (image.getWidth() > 400 && image.getHeight() > 400) {
            return Utils.scaleCenterCrop(image, 400, 400);
        }
        return image;
    }

    private void choseImageDevice(int idRequest) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.title_select_picture)), idRequest);
    }

    private void openBackCamera(int requestId) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = Uri.fromFile(file);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, requestId);
    }

    private void updateProfilePicture(JSONObject postData) {
        new DownloadAsyncTask(MyProfileEditActivity.this, Constants.UPDATE_PROFILE_PICKTURE, Constants.ID_METHOD_UPDATE_PROFILE_PICTURE, MyProfileEditActivity.this, true,
                DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
    }

    private void updateProfile(JSONObject postData) {
        new DownloadAsyncTask(MyProfileEditActivity.this, Constants.UPDATE_PROFILE, Constants.ID_METHOD_UPDATE_PROFILE, MyProfileEditActivity.this, true,
                DownloadAsyncTask.HTTP_VERB.POST.getVal(), postData.toString());
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(MyProfileEditActivity.this, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_gender, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.action_male:
                        txtGender.setText(context.getResources().getString(R.string.menu_male));
                        break;
                    case R.id.action_female:
                        txtGender.setText(context.getResources().getString(R.string.menu_female));
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        popup.show();
    }

    private void selectContryCode() {
        final CountryPicker picker = CountryPicker.newInstance(getString(R.string.title_select_country));
        picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
        picker.setListener(new CountryPickerListener() {
            @Override
            public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                countryCode = code;
//                ivFlag.setBackground(getResources().getDrawable(flagDrawableResID));
//                countryCode = Integer.parseInt(dialCode.substring(1,dialCode.length()));
                txtCountry.setText(name);
                picker.dismiss();
            }
        });
    }

    private void onSave() {
        String username = txtNickName.getText().toString();
        String description = txtStatus.getText().toString();
        String gender = txtGender.getText().toString();
        if (username.equals("")) {
            Utils.showSimpleDialogAlert(MyProfileEditActivity.this, getString(R.string.msg_error_enter_username));
        } else if (description.equals("")) {
            Utils.showSimpleDialogAlert(MyProfileEditActivity.this, getString(R.string.msg_error_please_enter_desription));
        } else if (gender.equals("")) {
            Utils.showSimpleDialogAlert(MyProfileEditActivity.this, getString(R.string.msg_error_please_enter_gender));
        } else if (countryCode.equals("")) {
            Utils.showSimpleDialogAlert(MyProfileEditActivity.this, getString(R.string.msg_error_choose_country));
        } else {
            try {
                JSONObject postData = new JSONObject();
                postData.put("username", username);
                postData.put("gender", gender.toLowerCase());
                postData.put("description", description);
                postData.put("country_code", countryCode);
                postData.put("token", Prefs.getUserInfo().getToken());
                Log.d("Info", "onSave: " + postData.toString());
                updateProfile(postData);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ibChooseAvatar:
                new DialogChooseImage(MyProfileEditActivity.this, MyProfileEditActivity.this).show();
                chooseProfile = true;
                chooseBackground = false;
                break;
            case R.id.ibChooseBackground:
                new DialogChooseImage(MyProfileEditActivity.this, MyProfileEditActivity.this).show();
                chooseProfile = false;
                chooseBackground = true;
                break;
            case R.id.ibtnBack:
                onBackPressed();
                break;
            case R.id.txtGender:
                showPopupMenu(txtGender);
                break;
            case R.id.txtCountry:
                selectContryCode();
                break;
            case R.id.tvCancel:
                finish();
                break;
            case R.id.tvSave:
                onSave();
                break;
            case R.id.llSave:
                onSave();
                break;
            case R.id.llCancel:
                onSave();
                break;
            default:
                break;
        }
    }

    @Override
    public void popUpCallback(Object data, int processID, Object obj, int num, int index) {
        if (processID == Constants.ID_POPUP_CHOOSE_IMAGE) {
            if (num == Constants.ID_POPUP_TAKE_PHOTO) {
                openBackCamera(ID_TAKE_PHOTO);
            } else if (num == Constants.ID_POPUP_CHOSE_PHOTO_LIBRARY) {
                choseImageDevice(ID_CHOOSE_FORM_LIBRARY);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == ID_CHOOSE_FORM_LIBRARY) {
                    Uri uri = data.getData();
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    if (chooseProfile) {
                        Bitmap bmResize = checkImageProfilePhoto(bitmap);
                        if (bmResize != null) {
                            CropImage.activity(uri).setAutoZoomEnabled(false).setMinCropWindowSize(800, 800).start(this);
                        }
                    } else if (chooseBackground) {
                        Bitmap bmResize = checkImageBackground(bitmap);
                        if (bmResize != null) {
                            CropImage.activity(uri).setAutoZoomEnabled(false).setMinCropWindowSize(1242, 615).start(this);
                        }
                    }
                } else if (requestCode == ID_TAKE_PHOTO) {
                    imgFile = new File(pictureImagePath);
                    if (chooseProfile) {
                        if (imgFile.exists()) {
                            Uri uri = Uri.fromFile(imgFile);
                            CropImage.activity(uri).setMinCropWindowSize(800, 800).start(this);
                        }
                    } else if (chooseBackground) {
                        if (imgFile.exists()) {
                            Uri uri = Uri.fromFile(imgFile);
                            CropImage.activity(uri).setMinCropWindowSize(1242, 615).start(this);
                        }
                    }
                } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), result.getUri());
                    if (chooseProfile) {
                        Picasso.with(context).load(result.getUri()).fit().centerCrop().transform(new CircleTransform()).into(ivAvatar);
                        String imageData = Utils.encodeFileToBase64Binary(bitmap);
                        JSONObject postData = new JSONObject();
                        postData.put("photo", imageData);
                        postData.put("photo_type", "profile");
                        postData.put("token", Prefs.getUserInfo().getToken());
                        updateProfilePicture(postData);
                    } else if (chooseBackground) {
                        Picasso.with(context).load(result.getUri()).fit().centerCrop().into(ivBackground);
                        String imageData = Utils.encodeFileToBase64Binary(bitmap);
                        JSONObject postData = new JSONObject();
                        postData.put("photo", imageData);
                        postData.put("photo_type", "background");
                        postData.put("token", Prefs.getUserInfo().getToken());
                        updateProfilePicture(postData);
                    }
                    if (!pictureImagePath.equals("")) {
                        if (imgFile.exists())
                            imgFile.delete();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void jsonCallback(Object data, int processID, int index) {
        if (processID == Constants.ID_METHOD_UPDATE_PROFILE) {
            if (data != null) {
                Response response = (Response) data;
                int errorCode = response.getError_code();
                String msg = response.getMessage();
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if (errorCode == Constants.ERROR_CODE_SUCCESS) {
                    onBackPressed();
                } else {
                    Utils.showSimpleDialogAlert(MyProfileEditActivity.this, msg);
                }
            } else {
                Utils.showSimpleDialogAlert(MyProfileEditActivity.this, getString(R.string.alert_unexpected_error));
            }
        }else if(processID == Constants.ID_METHOD_UPDATE_PROFILE_PICTURE){
            if(data != null){
                Response response = (Response) data;
                if(response.getError_code() == Constants.ERROR_ACCOUNT_SUSPENDED){
                    new DialogAccountSuspended(context, "", context.getString(R.string.title_account_suspended)).show();
                }else if(response.getError_code() == Constants.ERROR_TOKEN_INVALID){
                    new DialogInvalidToken(context, response.getMessage(), context.getString(R.string.app_name)).show();
                }else if(response.getError_code() == Constants.ERROR_CODE_SUCCESS){
                    if(response.getData() != null){
                        AccountInfo accountInfo = (AccountInfo) response.getData();
                        Prefs.setUserInfo(accountInfo);
                        sendBookingEvent(Constants.PUSH_RELOAD_MENU);
                    }
                }else {
                    Utils.showSimpleDialogAlert(context, response.getMessage());
                }
            }else
                Utils.showSimpleDialogAlert(context, getString(R.string.alert_unexpected_error));
        }
    }

    @Override
    public void jsonError(String msg, int processID) {
        Utils.showSimpleDialogAlert(MyProfileEditActivity.this, msg);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void sendBookingEvent(String type){
        EventBus.getDefault().post(type);
    }
}
