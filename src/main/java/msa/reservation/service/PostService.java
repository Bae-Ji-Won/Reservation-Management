package msa.reservation.service;

import lombok.RequiredArgsConstructor;
import msa.reservation.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void create(String title, String body, String userName){
        // user find

        // post save

        //return
    }
}
