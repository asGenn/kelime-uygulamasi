package com.example.kelime_uygulamasi.repository;

import android.util.Log;

import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChatGptRepository {

    String token = System.getenv("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");
    OpenAiService service = new OpenAiService("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");


    public void getChatComplation(){
        final List<ChatMessage> messages = new ArrayList<>();
        final ChatMessage systemMessage = new ChatMessage(ChatMessageRole.SYSTEM.value(),"your are english story teller. \n" +
                "I'll give you Json format :\n" +
                "{\n" +
                "       \"words\":[\n" +
                "           ]\n" +
                "}\n" +
                "you have to return Json format like this :\n" +
                "{\n" +
                "    \"stroy\":\"\"\n" +
                "\n" +
                "}");

        messages.add(systemMessage);


        Single.fromCallable(() -> {
            ChatCompletionRequest chatCompletionRequest =
                    ChatCompletionRequest.builder().
                    model("gpt-3.5-turbo").messages(messages).
                    n(1).logitBias(new HashMap<>()).build();
            ChatCompletionResult chatCompletionResult = service.createChatCompletion(chatCompletionRequest);
           return chatCompletionResult;
        }).subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<ChatCompletionResult>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(ChatCompletionResult value) {
                        System.out.println(value.getChoices().get(0).getMessage().getContent()+"**********************");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d("error", "onError: "+e);
                    }
                });


    }

}
