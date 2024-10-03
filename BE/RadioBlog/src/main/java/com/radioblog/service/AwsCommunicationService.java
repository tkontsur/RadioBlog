package com.radioblog.service;

import software.amazon.awssdk.services.polly.model.StartSpeechSynthesisTaskResponse;

import java.util.concurrent.CompletableFuture;

public interface AwsCommunicationService {
    CompletableFuture<StartSpeechSynthesisTaskResponse> enqueueMp3FileGeneration(long blogPostId);
}
