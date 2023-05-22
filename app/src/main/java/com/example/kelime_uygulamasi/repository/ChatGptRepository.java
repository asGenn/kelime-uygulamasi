package com.example.kelime_uygulamasi.repository;


import androidx.lifecycle.MutableLiveData;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class ChatGptRepository {
    private final String model = "gpt-3.5-turbo";
    private final String  API_KEY = "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";
    private final OpenAiService service = new OpenAiService(API_KEY);
    private ChatCompletionRequest request;
    private ChatCompletionResult result;
    private List<ChatMessage> messages;
    private MutableLiveData<String> story;

    public ChatGptRepository() {
        messages = new ArrayList<>();
        story = new MutableLiveData<>();
    }

    public MutableLiveData<String> generateWordDetail(){
        ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(), "you are english random word genator" );
        messages.add(systemMessage);
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), "give me random word");
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
        return story;

    }

}
