package vn.cheesethank.jobhunter.domain.response;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.cheesethank.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
public class ResUpdateJobDTO {
    private long id;

    private String name;

    private String location;

    private double salary;

    private int quantity;

    private LevelEnum level;

    private Instant startDate;

    private Instant endDate;

    private boolean active;

    private List<String> skills;

    private Instant updatedAt;

    private String updatedBy;
}