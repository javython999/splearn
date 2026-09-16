package com.errday.splearn.domain.enrollment;

import com.errday.splearn.domain.AbstractEntity;
import com.errday.splearn.domain.course.Course;
import com.errday.splearn.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.NaturalId;

import java.time.LocalDateTime;

import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true, exclude = {"member", "course"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "UK_ENROLLMENT_MEMBER_COURSE", columnNames = {"member_id", "course_id"}))
public class Enrollment extends AbstractEntity {
    @NaturalId
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Member member;

    @NaturalId
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Course course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EnrollmentStatus status;

    @Column(nullable = false)
    private LocalDateTime enrolledAt;

    private LocalDateTime completedAt;

    public static Enrollment enroll(Member member, Course course) {
        member.ensureActive();
        course.ensurePublished();

        Enrollment enrollment = new Enrollment();
        enrollment.member = member;
        enrollment.course = course;
        enrollment.status = EnrollmentStatus.ENROLLED;
        enrollment.enrolledAt = LocalDateTime.now();
        return enrollment;
    }

    public void startStudying() {
        state(status == EnrollmentStatus.ENROLLED, "수강 상태가 ENROLLED가 아닙니다.");

        this.status = EnrollmentStatus.STUDYING;
    }

    public void complete() {
        state(status == EnrollmentStatus.STUDYING, "수강 상태가 STUDYING이 아닙니다.");

        this.status = EnrollmentStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }
}
