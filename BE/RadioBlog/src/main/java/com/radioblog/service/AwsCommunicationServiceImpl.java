package com.radioblog.service;

import com.radioblog.entity.BlogPost;
import com.radioblog.repository.BlogPostsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.Engine;
import software.amazon.awssdk.services.polly.model.LanguageCode;
import software.amazon.awssdk.services.polly.model.OutputFormat;
import software.amazon.awssdk.services.polly.model.StartSpeechSynthesisTaskRequest;
import software.amazon.awssdk.services.polly.model.StartSpeechSynthesisTaskResponse;
import software.amazon.awssdk.services.polly.model.VoiceId;

import java.util.concurrent.CompletableFuture;

@Service
public class AwsCommunicationServiceImpl implements AwsCommunicationService {
    private final PollyClient pollyClient;
    private final BlogPostsRepository blogPostsRepository;
    private final Logger logger = LoggerFactory.getLogger(AwsCommunicationServiceImpl.class);

    @Value("${aws.s3.output.bucket}")
    private String outputBucket;
    @Value("${aws.sns.output.topic}")
    private String outputTopic;

    @Autowired
    public AwsCommunicationServiceImpl(PollyClient pollyClient, BlogPostsRepository blogPostsRepository) {
        this.pollyClient = pollyClient;
        this.blogPostsRepository = blogPostsRepository;
    }

    @Override
    @Async
    public CompletableFuture<StartSpeechSynthesisTaskResponse> enqueueMp3FileGeneration(long blogPostId) {
        BlogPost blogPost = blogPostsRepository.findById(blogPostId).orElseThrow();
        logger.debug("Generating MP3 file for blog post {}: {}", blogPostId, blogPost.getTitle());
        String s3Key = "blog-" + blogPost.getBlog().getId() + "/post-" + blogPost.getId();

        StartSpeechSynthesisTaskRequest.Builder synthesizeSpeechRequest = StartSpeechSynthesisTaskRequest.builder()
                .text(blogPost.getTitle() + ". " + blogPost.getContent())
                .outputFormat(OutputFormat.MP3)
                .outputS3BucketName(outputBucket)
                .outputS3KeyPrefix(s3Key)
                .voiceId(VoiceId.AMY)
                .engine(Engine.NEURAL)
                .languageCode(LanguageCode.EN_GB);

        if(outputTopic != null) {
            synthesizeSpeechRequest.snsTopicArn(outputTopic);
        }

        StartSpeechSynthesisTaskResponse request = pollyClient.startSpeechSynthesisTask(synthesizeSpeechRequest.build());
        logger.debug("Generation started for blog post {}: {}", blogPostId, request.synthesisTask().outputUri());
        blogPost.setMp3Url(request.synthesisTask().outputUri());
        blogPostsRepository.save(blogPost);
        logger.debug("MP3 URL saved for blog post {}: {}", blogPostId, blogPost.getMp3Url());
        return CompletableFuture.completedFuture(request);
    }
}
