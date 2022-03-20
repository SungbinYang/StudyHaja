package com.studyhaja.modules.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * packageName : com.studyhaja.service.tag
 * fileName : TagService
 * author : rovert
 * date : 2022/03/13
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/13       rovert         최초 생성
 */

@Service
@Transactional
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Tag findOrCreateNew(String tagTitle) {
        Tag tag = tagRepository.findByTitle(tagTitle);

        if (tag == null) {
            tag = tagRepository.save(Tag.builder().title(tagTitle).build());
        }

        return tag;
    }
}
