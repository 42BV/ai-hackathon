package nl._42.springai.hackathon.domain.publication;

import java.util.HashMap;
import java.util.Map;

public enum Tag {
    // Original Tags
    VIOLENT,
    DEPRESSED,
    QUIET,
    DRIVER_LICENSE,
    UNDER_25,
    BETWEEN_26_TO_65,
    ABOVE_65,
    CAN_SWIM,
    HAY_FEVER,
    WEAK,
    STRONG,
    INTELLIGENT,
    MENTALLY_CHALLENGED,
    MARRIED,
    NOT_MARRIED,

    // Additional Tags

    // Personal Attributes
    CONFIDENT,
    INTROVERTED,
    EXTROVERTED,
    OPTIMISTIC,
    PESSIMISTIC,

    // Physical Abilities
    DISABLED,
    ABLE_BODIED,
    VISION_IMPAIRED,
    HEARING_IMPAIRED,

    // Health Conditions
    DIABETIC,
    ASTHMATIC,
    ALLERGIC_TO_PEANUTS,

    // Educational Level
    HIGH_SCHOOL_DIPLOMA,
    BACHELORS_DEGREE,
    MASTERS_DEGREE,
    DOCTORATE_DEGREE,

    // Employment Status
    EMPLOYED,
    UNEMPLOYED,
    RETIRED,
    STUDENT,

    // Marital and Family Status
    SINGLE,
    DIVORCED,
    WIDOWED,
    PARENT,
    CHILDLESS,

    // Lifestyle Choices
    SMOKER,
    NON_SMOKER,
    DRINKER,
    NON_DRINKER,
    VEGETARIAN,
    VEGAN,

    // Interests and Hobbies
    SPORTS_ENTHUSIAST,
    MUSIC_LOVER,
    TRAVELER,
    READER,

    // Languages
    ENGLISH_SPEAKER,
    SPANISH_SPEAKER,
    MULTILINGUAL,

    // Pet Ownership
    PET_OWNER,
    NO_PETS,

    // Vehicle Ownership
    CAR_OWNER,
    BICYCLE_OWNER,
    PUBLIC_TRANSPORT_USER,

    // Internet Usage
    INTERNET_USER,
    NON_INTERNET_USER,

    // Housing Status
    HOMEOWNER,
    RENTER,

    // Citizenship and Residency
    CITIZEN,
    NON_CITIZEN,
    RESIDENT,

    // Military Service
    VETERAN,
    ACTIVE_DUTY,
    NON_MILITARY,

    // Insurance Coverage
    HEALTH_INSURANCE,
    NO_HEALTH_INSURANCE,
    LIFE_INSURANCE,

    // Work Schedule
    FULL_TIME,
    PART_TIME,
    FREELANCER,

    // Travel Documents
    PASSPORT_HOLDER,
    NO_PASSPORT;

    // Map to hold categories and their associated tags
    public static final Map<String, Tag[]> CATEGORIES = new HashMap<>();

    static {
        CATEGORIES.put("Personal Attributes", new Tag[] {
                CONFIDENT,
                INTROVERTED,
                EXTROVERTED,
                OPTIMISTIC,
                PESSIMISTIC,
                VIOLENT,
                DEPRESSED,
                QUIET,
                INTELLIGENT,
                MENTALLY_CHALLENGED,
                WEAK,
                STRONG
        });

        CATEGORIES.put("Physical Abilities", new Tag[] {
                DISABLED,
                ABLE_BODIED,
                VISION_IMPAIRED,
                HEARING_IMPAIRED,
                CAN_SWIM
        });

        CATEGORIES.put("Health Conditions", new Tag[] {
                DIABETIC,
                ASTHMATIC,
                HAY_FEVER,
                ALLERGIC_TO_PEANUTS
        });

        CATEGORIES.put("Educational Level", new Tag[] {
                HIGH_SCHOOL_DIPLOMA,
                BACHELORS_DEGREE,
                MASTERS_DEGREE,
                DOCTORATE_DEGREE
        });

        CATEGORIES.put("Employment Status", new Tag[] {
                EMPLOYED,
                UNEMPLOYED,
                RETIRED,
                STUDENT
        });

        CATEGORIES.put("Marital and Family Status", new Tag[] {
                MARRIED,
                NOT_MARRIED,
                SINGLE,
                DIVORCED,
                WIDOWED,
                PARENT,
                CHILDLESS
        });

        CATEGORIES.put("Lifestyle Choices", new Tag[] {
                SMOKER,
                NON_SMOKER,
                DRINKER,
                NON_DRINKER,
                VEGETARIAN,
                VEGAN
        });

        CATEGORIES.put("Interests and Hobbies", new Tag[] {
                SPORTS_ENTHUSIAST,
                MUSIC_LOVER,
                TRAVELER,
                READER
        });

        CATEGORIES.put("Languages", new Tag[] {
                ENGLISH_SPEAKER,
                SPANISH_SPEAKER,
                MULTILINGUAL
        });

        CATEGORIES.put("Pet Ownership", new Tag[] {
                PET_OWNER,
                NO_PETS
        });

        CATEGORIES.put("Vehicle Ownership", new Tag[] {
                CAR_OWNER,
                BICYCLE_OWNER,
                PUBLIC_TRANSPORT_USER,
                DRIVER_LICENSE
        });

        CATEGORIES.put("Internet Usage", new Tag[] {
                INTERNET_USER,
                NON_INTERNET_USER
        });

        CATEGORIES.put("Housing Status", new Tag[] {
                HOMEOWNER,
                RENTER
        });

        CATEGORIES.put("Citizenship and Residency", new Tag[] {
                CITIZEN,
                NON_CITIZEN,
                RESIDENT
        });

        CATEGORIES.put("Military Service", new Tag[] {
                VETERAN,
                ACTIVE_DUTY,
                NON_MILITARY
        });

        CATEGORIES.put("Insurance Coverage", new Tag[] {
                HEALTH_INSURANCE,
                NO_HEALTH_INSURANCE,
                LIFE_INSURANCE
        });

        CATEGORIES.put("Work Schedule", new Tag[] {
                FULL_TIME,
                PART_TIME,
                FREELANCER
        });

        CATEGORIES.put("Travel Documents", new Tag[] {
                PASSPORT_HOLDER,
                NO_PASSPORT
        });

        CATEGORIES.put("Age Groups", new Tag[] {
                UNDER_25,
                BETWEEN_26_TO_65,
                ABOVE_65
        });
    }
}
