package com.example.kelime_uygulamasi.repository;
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

import java.time.Duration;
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
    private final String API_KEY = "OPEN_AI_KEY";
    private final OpenAiService service = new OpenAiService(API_KEY, Duration.ofSeconds(30));
    private ChatCompletionRequest request;
    private ChatCompletionResult result;
    private List<ChatMessage> messages;
    private MutableLiveData<String> story;

    public ChatGptRepository() {
        messages = new ArrayList<>();
        story = new MutableLiveData<>();
    }

    public MutableLiveData<String > generateWordDetail() {
        FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getUid();
        MutableLiveData<String> story = new MutableLiveData<>();

        mFirestore.collection("User").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    List<Map<String, Object>> wordList = (List<Map<String, Object>>) documentSnapshot.get("wordList");
                    List<String> words = new ArrayList<>();
                    if (wordList != null && !wordList.isEmpty()) {

                        for (Map<String, Object> word : wordList) {

                            words.add(word.get("word").toString());
                        }
                        System.out.println(words);

                    }
                    ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "I'l give you list of string you return story about words.the story around 50 words ");
                    ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), words.toString() );
                    messages.add(systemMessage);
                    messages.add(userMessage);
                    Single.fromCallable(() -> {
                                request = ChatCompletionRequest
                                        .builder()
                                        .messages(messages)
                                        .model(model)
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
                                    System.out.println(chatCompletionResult.getChoices().get(0).getMessage().getContent());
                                    story.postValue(chatCompletionResult.getChoices().get(0).getMessage().getContent());
                                }

                                @Override
                                public void onError(@NonNull Throwable e) {
                                    e.printStackTrace();
                                }
                            });


                }
            }
        });
        return story;


    }



}
