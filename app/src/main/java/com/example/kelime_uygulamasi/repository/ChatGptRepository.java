package com.example.kelime_uygulamasi.repository;


import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ChatGptRepository {
    private final String model = "gpt-3.5-turbo";
    private final String API_KEY = "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";
    private final OpenAiService service = new OpenAiService(API_KEY);
    private ChatCompletionRequest request;
    private ChatCompletionResult result;
    private List<ChatMessage> messages;
    private MutableLiveData<String> story;

    public ChatGptRepository() {
        messages = new ArrayList<>();
        story = new MutableLiveData<>();
    }

    public MutableLiveData<String> generateWordDetail() {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();

        MutableLiveData<String> story = new MutableLiveData<>();

        mFirestore.collection("User").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    List<Map<String, Object>> wordList = (List<Map<String, Object>>) documentSnapshot.get("wordList");

                    if (wordList != null && !wordList.isEmpty()) {
                        JSONArray jsonWords = new JSONArray();
                        for (Map<String, Object> word : wordList) {
                            jsonWords.put(word.toString());
                        }

                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("words", jsonWords);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String jsonString = jsonObject.toString();

                        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.name().toLowerCase(), "your are english story teller. \n" +
                                "I'll give you Json format :\n" +
                                "{\n" +
                                "       \"words\":[\n" +
                                "           ]\n" +
                                "}\n" +
                                "you have to return Json format like this :\n" +
                                "{\n" +
                                "    \"story\":\"\"\n" +
                                "\n" +
                                "}");
                        messages.add(systemMessage);
                        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.name().toLowerCase(), jsonString);
                        messages.add(userMessage);

                        Single.fromCallable(() -> {
                                    request = ChatCompletionRequest
                                            .builder()
                                            .messages(messages)
                                            .model(model)
                                            .temperature(1.0)
                                            .topP(1.0)
                                            .build();
                                    result = service.createChatCompletion(request);
                                    return result;
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new SingleObserver<ChatCompletionResult>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {

                                    }

                                    @Override
                                    public void onSuccess(@NonNull ChatCompletionResult chatCompletionResult) {
                                        story.postValue(chatCompletionResult.getChoices().get(0).getMessage().getContent());
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        e.printStackTrace();
                                    }
                                });
                    } else {
                        Log.d(TAG, "Kullanıcının eklediği kelime yok.");
                    }
                }
            }
        });

        return story;
    }

}
