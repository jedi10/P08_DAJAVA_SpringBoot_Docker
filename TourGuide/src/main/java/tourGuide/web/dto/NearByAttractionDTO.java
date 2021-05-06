package tourGuide.web.dto;

import rewardCentral.RewardCentral;
import tourGuide.domain.NearByAttraction;
import tourGuide.domain.User;

/**
 * <b>Used with NearByUserAttractionDTO</b>
 * @see tourGuide.web.dto.NearByUserAttractionDTO
 */
public class NearByAttractionDTO {

        private String attractionName;
        private Double attractionLat;
        private Double attractionLong;
        private Double distance;
        private int rewardsPoints;

        /**
         * <b>Constructor</b>
         * @param nearByAttraction mandatory - Attraction Type
         * @param user mandatory - User Type
         */
        public NearByAttractionDTO(NearByAttraction nearByAttraction, User user, RewardCentral rewardCentral) {
            this.attractionName = nearByAttraction.getAttractionName();
            this.attractionLat = nearByAttraction.getAttractionLat();
            this.attractionLong = nearByAttraction.getAttractionLong();
            this.distance = nearByAttraction.getDistance();
            calculateAttractionRewards(nearByAttraction, user, rewardCentral);
        }

        public void calculateAttractionRewards(NearByAttraction nearByAttraction, User user, RewardCentral rewardCentral){
            this.rewardsPoints = rewardCentral.getAttractionRewardPoints(nearByAttraction.getAttractionId(), user.getUserId());
        }

        public String getAttractionName() {
            return attractionName;
        }

        public void setAttractionName(String attractionName) {
            this.attractionName = attractionName;
        }

        public Double getAttractionLat() {
            return attractionLat;
        }

        public void setAttractionLat(Double attractionLat) {
            this.attractionLat = attractionLat;
        }

        public Double getAttractionLong() {
            return attractionLong;
        }

        public void setAttractionLong(Double attractionLong) {
            this.attractionLong = attractionLong;
        }

        public Double getDistance() {
            return distance;
        }

        public void setDistance(Double distance) {
            this.distance = distance;
        }

        public int getRewardsPoints() {
            return rewardsPoints;
        }

        public void setRewardsPoints(int rewardsPoints) {
            this.rewardsPoints = rewardsPoints;
        }
}


//https://www.javaguides.net/2019/04/jackson-ignore-fields-or-properties-on-serialization.html

