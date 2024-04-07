//package com.example.trytranslator;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//
//import android.text.method.ScrollingMovementMethod;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.PopupMenu;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.mlkit.common.model.DownloadConditions;
//import com.google.mlkit.nl.translate.TranslateLanguage;
//import com.google.mlkit.nl.translate.Translation;
//import com.google.mlkit.nl.translate.Translator;
//import com.google.mlkit.nl.translate.TranslatorOptions;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Locale;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link Translation_fragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class Translation_fragment extends Fragment {
//
//
//
//    public Translation_fragment() {
//        // Required empty public constructor
//    }
//
//
//
//    // TODO: Rename and change types and number of parameters
//    public static Translation_fragment newInstance() {
//        return new Translation_fragment();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.translation_fragment, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        final EditText sourceLanguageEt = view.findViewById(R.id.sourceText);
//        final TextView destinationLanguageTv =  view.findViewById(R.id.targetText);
//        destinationLanguageTv.setMovementMethod(new ScrollingMovementMethod());
//        final Button sourceLanguageChooseBtn = view.findViewById(R.id.sourceLangSelector);
//        final  Button destinationLanguageChoseBtn = view.findViewById(R.id.targetLangSelector);
//        final  Button  translateBtn = view.findViewById(R.id.translateBtn);
//        final ArrayList<ModelLanguage> languageArrayList
////        private Translator translator;
////        private ProgressDialog progressDialog;
////        private ArrayList<ModelLanguage> languageArrayList;
////        private String sourceLanguageCode = "en";
////        private String sourceLanguageTitle = "English";
////        private String destinationLanguageCode = "ru";
////        private String destinationLanguageTitle = "Russian";
////        private String sourceLanguageText = "";
//
////        progressDialog = new ProgressDialog(this);
////        progressDialog.setTitle("Please wait");
////        progressDialog.setCanceledOnTouchOutside(false);
//        loadAvailableLanguages();
//        sourceLanguageChooseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sourceLanguageChoose();
//            }
//        });
//        destinationLanguageChoseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                destinationLanguageChoose();
//            }
//        });
//        translateBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String sourceLanguageText = sourceLanguageEt.getText().toString().trim();
//                if (sourceLanguageText.isEmpty()){
//                    Toast.makeText(getContext(), "Enter text", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    TranslatorOptions translatorOptions = new TranslatorOptions.Builder()
//                            .setSourceLanguage(sourceLanguageCode)
//                            .setTargetLanguage(destinationLanguageCode)
//                            .build();
//                    Translator translator = Translation.getClient(translatorOptions);
//                    DownloadConditions downloadConditions = new DownloadConditions.Builder()
//                            .requireWifi().build();
//                    translator.downloadModelIfNeeded(downloadConditions);
//                    translator.translate(sourceLanguageText);
//                    destinationLanguageTv.setText(translatedText);
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    translator.translate(sourceLanguageText)
//                                            .addOnSuccessListener(new OnSuccessListener<String>() {
//                                                @Override
//                                                public void onSuccess(String translatedText) {
//                                                    destinationLanguageTv.setText(translatedText);
//                                                }
//                                            })
//                                            .addOnFailureListener(new OnFailureListener() {
//                                                @Override
//                                                public void onFailure(@NonNull Exception e) {
//                                                    progressDialog.dismiss();
//                                                }
//                                            });
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    progressDialog.dismiss();
//                                }
//                            });
//                }
//                }
//            }
//        });
//    }
//
//    private void validateData() {
//        String sourceLanguageText = sourceLanguageEt.getText().toString().trim();
//        if (sourceLanguageText.isEmpty()){
//            Toast.makeText(getContext(), "Enter text", Toast.LENGTH_SHORT).show();
//        }
//        else{
//            startTranslations();
//        }
//    }
//
//    private void startTranslations() {
//        progressDialog.setMessage("Processing language model...");
//        progressDialog.show();
//        translatorOptions = new TranslatorOptions.Builder()
//                .setSourceLanguage(sourceLanguageCode)
//                .setTargetLanguage(destinationLanguageCode)
//                .build();
//        translator = Translation.getClient(translatorOptions);
//        DownloadConditions downloadConditions = new DownloadConditions.Builder()
//                .requireWifi().build();
//        translator.downloadModelIfNeeded(downloadConditions)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        progressDialog.setMessage("Translating...");
//                        progressDialog.show();
//                        translator.translate(sourceLanguageText)
//                                .addOnSuccessListener(new OnSuccessListener<String>() {
//                                    @Override
//                                    public void onSuccess(String translatedText) {
//                                        progressDialog.dismiss();
//                                        destinationLanguageTv.setText(translatedText);
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        progressDialog.dismiss();
//                                    }
//                                });
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        progressDialog.dismiss();
//                    }
//                });
//    }
//
//    private void sourceLanguageChoose(){
//        PopupMenu popupMenu = new PopupMenu(this, sourceLanguageChooseBtn);
//        for (int i = 0; i<languageArrayList.size();i++){
//            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).languageTitle);
//        }
//        popupMenu.show();
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int position = item.getItemId();
//                sourceLanguageCode = languageArrayList.get(position).languageCode;
//                sourceLanguageTitle = languageArrayList.get(position).languageTitle;
//                sourceLanguageChooseBtn.setText(sourceLanguageTitle);
//                return false;
//            }
//        });
//    }
//    private void destinationLanguageChoose(){
//        PopupMenu popupMenu = new PopupMenu(this, destinationLanguageChoseBtn);
//        for (int i = 0; i<languageArrayList.size();i++){
//            popupMenu.getMenu().add(Menu.NONE,i,i,languageArrayList.get(i).languageTitle);
//        }
//        popupMenu.show();
//        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int position = item.getItemId();
//                destinationLanguageCode = languageArrayList.get(position).languageCode;
//                destinationLanguageTitle = languageArrayList.get(position).languageTitle;
//                destinationLanguageChoseBtn.setText(destinationLanguageTitle);
//                return false;
//            }
//        });
//    }
//    private void loadAvailableLanguages() {
//        ArrayList<ModelLanguage> languageArrayList = new ArrayList<>();
//        List<String> languageCodeList = TranslateLanguage.getAllLanguages();
//        for (String languageCode:languageCodeList){
//            String languageTitle = new Locale(languageCode).getDisplayLanguage();
//            ModelLanguage modelLanguage = new ModelLanguage(languageCode,languageTitle);
//            languageArrayList.add(modelLanguage);
//        }
//        Collections.sort(languageArrayList,(a, b)->a.languageTitle.compareTo(b.languageTitle));
//    }
//}