package com.example.reactorPractice.reactor;

import com.example.reactorPractice.reactor.repository.ArticleReactorRepository;
import com.example.reactorPractice.reactor.repository.FollowReactorRepository;
import com.example.reactorPractice.reactor.repository.ImageReactorRepository;
import com.example.reactorPractice.reactor.repository.UserReactorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UserReactorService {
    private final UserReactorRepository userRepository;
    private final ArticleReactorRepository articleRepository;
    private final ImageReactorRepository imageRepository;
    private final FollowReactorRepository followRepository;

}
