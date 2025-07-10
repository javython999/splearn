package com.errday.splearn.domain.member;

import com.errday.splearn.domain.AbstractEntity;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;

import static java.util.Objects.requireNonNull;
import static org.springframework.util.Assert.state;

@Entity
@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberDetail extends AbstractEntity {

   @Embedded
   private Profile profile;

   private String introduction;

   private LocalDateTime registeredAt;

   private LocalDateTime activatedAt;

   private LocalDateTime deactivatedAt;

   static MemberDetail create() {
      MemberDetail memberDetail = new MemberDetail();
      memberDetail.registeredAt = LocalDateTime.now();
      return memberDetail;
   }

   void activate() {
      Assert.isTrue(activatedAt == null, "Activated at is already set");
      this.activatedAt = LocalDateTime.now();
   }

   public void deactivate() {
      Assert.isTrue(deactivatedAt == null, "Deactivated at is already set");
      this.deactivatedAt = LocalDateTime.now();
   }

   void updateInfo(MemberInfoUpdateRequest updateRequest) {
      this.profile = new Profile(updateRequest.profileAddress());
      this.introduction = Objects.requireNonNull(updateRequest.introduction());
   }
}
