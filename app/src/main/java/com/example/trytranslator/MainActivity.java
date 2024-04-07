package com.example.trytranslator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.google.android.material.button.MaterialButton;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText sourceLanguageEt;
    private TextView destinationLanguageTv;
    private MaterialButton sourceLanguageChooseBtn;
    private MaterialButton destinationLanguageChoseBtn;
    private MaterialButton translateBtn;
    private Button switchLanguageBtn;
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
            ModelLanguage modelLanguage = new ModelLanguage(languageCode,languageTitle);
            languageArrayList.add(modelLanguage);
        }
        Collections.sort(languageArrayList,(a,b)->a.languageTitle.compareTo(b.languageTitle));
    }


}