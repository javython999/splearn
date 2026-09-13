package com.errday.splearn.domain.course;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.Size;

public record CourseUpdateInfo(
        @Size(min = 2, max = 100) String title,
        @Nullable String description
) {
}
