package com.example.trytranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.TextRecognizerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText sourceLanguageEt;
    private TextView destinationLanguageTv;
    private Button sourceLanguageChooseBtn;
    private Button destinationLanguageChoseBtn;
    private Button translateBtn;
    private Button switchLanguageBtn,btn_camera, btn_gallery;;
    private TranslatorOptions translatorOptions;
    private Translator translator;
    private ProgressDialog progressDialog;
    private ArrayList<ModelLanguage> languageArrayList;
    private String sourceLanguageCode = "en";
    private String sourceLanguageTitle = "Английский";
    private String destinationLanguageCode = "ru";
    private String destinationLanguageTitle = "Русский";
    private String sourceLanguageText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.translation_fragment);
        switchLanguageBtn = findViewById(R.id.buttonSwitchLang);
        sourceLanguageEt = findViewById(R.id.sourceText);
        destinationLanguageTv = findViewById(R.id.targetText);
        destinationLanguageTv.setMovementMethod(new ScrollingMovementMethod());
        sourceLanguageChooseBtn = findViewById(R.id.sourceLangSelector);
        destinationLanguageChoseBtn = findViewById(R.id.targetLangSelector);
        btn_camera = findViewById(R.id.buttonCamera);
        btn_gallery = findViewById(R.id.buttonGalery);
        translateBtn = findViewById(R.id.translateBtn);


        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);
        loadAvailableLanguages();
        switchLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeLanguges();
            }
        });
        sourceLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sourceLanguageChoose();
            }
        });
        destinationLanguageChoseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destinationLanguageChoose();
            }
        });
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });

        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To take picture from camera

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code

            }
        });
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //To pick photo from gallery

                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 1);//one can be replaced with any action code
            }
        });
    }

    private void changeLanguges() {
        String tempCode = sourceLanguageCode;
        String tempTitle = sourceLanguageTitle;
        sourceLanguageCode = destinationLanguageCode;
        sourceLanguageTitle = destinationLanguageTitle;
        destinationLanguageCode = tempCode;
        destinationLanguageTitle = tempTitle;
        sourceLanguageText = destinationLanguageTv.getText().toString();
        sourceLanguageEt.setText(sourceLanguageText);
        sourceLanguageChooseBtn.setText(sourceLanguageTitle);
        destinationLanguageChoseBtn.setText(destinationLanguageTitle);
    }

    private void validateData() {
        sourceLanguageText = sourceLanguageEt.getText().toString().trim();
        if (sourceLanguageText.isEmpty()){
            Toast.makeText(this, "Enter text", Toast.LENGTH_SHORT).show();
        }
        else{
            startTranslations();
        }
    }

    private void startTranslations() {
        progressDialog.setMessage("Processing language model...");
        progressDialog.show();
        translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguageCode)
                .setTargetLanguage(destinationLanguageCode)
                .build();
        translator = Translation.getClient(translatorOptions);
        DownloadConditions downloadConditions = new DownloadConditions.Builder()
                .requireWifi().build();
        translator.downloadModelIfNeeded(downloadConditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.setMessage("Translating...");
                        progressDialog.show();
                        translator.translate(sourceLanguageText)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String translatedText) {
                                        progressDialog.dismiss();
                                        destinationLanguageTv.setText(translatedText);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                    }
                });
    }

    private void sourceLanguageChoose(){
        PopupMenu popupMenu = new PopupMenu(this, sourceLanguageChooseBtn);
        for (int i = 0; i<languageArrayList.size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).languageTitle);
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();
                sourceLanguageCode = languageArrayList.get(position).languageCode;
                sourceLanguageTitle = languageArrayList.get(position).languageTitle;
                sourceLanguageChooseBtn.setText(sourceLanguageTitle);
                return false;
            }
        });
    }
    private void destinationLanguageChoose(){
        PopupMenu popupMenu = new PopupMenu(this, destinationLanguageChoseBtn);
        for (int i = 0; i<languageArrayList.size();i++){
            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).languageTitle);
        }
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int position = item.getItemId();
                destinationLanguageCode = languageArrayList.get(position).languageCode;
                destinationLanguageTitle = languageArrayList.get(position).languageTitle;
                destinationLanguageChoseBtn.setText(destinationLanguageTitle);
                return false;
            }
        });
    }
    private void loadAvailableLanguages() {
        languageArrayList = new ArrayList<>();
        List<String> languageCodeList = TranslateLanguage.getAllLanguages();
        for (String languageCode:languageCodeList){
            String languageTitle = new Locale(languageCode).getDisplayLanguage();
            languageTitle = languageTitle.substring(0, 1).toUpperCase() + languageTitle.substring(1);
            ModelLanguage modelLanguage = new ModelLanguage(languageCode,languageTitle);
            languageArrayList.add(modelLanguage);
        }
        Collections.sort(languageArrayList,(a,b)->a.languageTitle.compareTo(b.languageTitle));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    detectText(imageBitmap);
                    //imageView.setImageBitmap(imageBitmap);
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                        detectText(imageBitmap);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    //imageView.setImageURI(selectedImage);
                }
                break;
        }
    }
    private void detectText(Bitmap selectedImage){
        InputImage image = InputImage.fromBitmap(selectedImage,0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(@NonNull Text text) {
                StringBuilder result = new StringBuilder();
                for (Text.TextBlock block : text.getTextBlocks()){
                    String blockText = block.getText();
                    for (Text.Line line:block.getLines()){
                        String lineText = line.getText();
                        for (Text.Element elemet:line.getElements()){
                            String elementText = elemet.getText();
                            result.append(elementText+" ");
                        }
                        result.append("\n");
                    }
                }
                sourceLanguageEt.setText(result);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }
}