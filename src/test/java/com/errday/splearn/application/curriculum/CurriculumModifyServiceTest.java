package com.errday.splearn.application.curriculum;

import com.errday.splearn.application.curriculum.provided.CurriculumCoordinator;
import com.errday.splearn.domain.curriculum.Curriculum;
import com.errday.splearn.support.stereotype.ApplicationServiceTest;
import com.errday.splearn.support.test.BaseApplicationServiceTest;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ApplicationServiceTest
@RequiredArgsConstructor
class CurriculumModifyServiceTest extends BaseApplicationServiceTest {
    final CurriculumCoordinator curriculumCoordinator;

    @Test
    void create() {
        Curriculum curriculum = curriculumCoordinator.create(prepareCourse().getId());

        assertThat(curriculum.getId()).isNotNull();
    }
}