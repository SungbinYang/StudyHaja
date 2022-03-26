package com.studyhaja.modules.tag;

import com.studyhaja.infra.AbstractContainerBaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName : com.studyhaja.modules.tag
 * fileName : TagRepositoryTest
 * author : rovert
 * date : 2022/03/26
 * description :
 * ===========================================================
 * DATE 			AUTHOR			 NOTE
 * -----------------------------------------------------------
 * 2022/03/26       rovert         최초 생성
 */

@DataJpaTest
class TagRepositoryTest extends AbstractContainerBaseTest {

    @Autowired
    TagRepository tagRepository;

    @Test
    @DisplayName("findAll() 테스트")
    void findAll() {
        Tag jpa = Tag.builder().title("jpa").build();
        tagRepository.save(jpa);

        Tag java = Tag.builder().title("JAVA").build();
        tagRepository.save(java);

        List<Tag> tags = tagRepository.findAll();
        assertEquals(2, tags.size());
    }
}