package com.errday.splearn.application.curriculum.required;

import com.errday.splearn.domain.curriculum.Section;
import org.springframework.data.repository.Repository;

public interface SectionRepository extends Repository<Section, Long> {
    void delete(Section section);
}
