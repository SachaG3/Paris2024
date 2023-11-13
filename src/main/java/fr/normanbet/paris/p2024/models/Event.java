package fr.normanbet.paris.p2024.models;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.normanbet.paris.p2024.models.types.EventStatusType;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import net.time4j.PrettyTime;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = "id")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDateTime dateEvent;

    private boolean isFinal;

    private EventStatusType status;

    @Column(length = 100)
    private String description;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    @OrderBy("order ASC")
    @JsonIgnore
    private Set<Quotation> quotations = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "previousEvents")
    @JsonIgnore
    private Set<Quotation> previousQuotations = new LinkedHashSet<>();

    @ManyToOne
    @JsonIgnore
    private Discipline discipline;

    @ManyToOne
    @JsonIgnore
    private Olympiad olympiad;

    @ManyToOne
    @JsonIgnore
    private Venue venue;

    @JsonIgnore
    public Sport getSport() {
        return this.discipline.getSport();
    }

    public boolean isTerminated() {
        return this.status == EventStatusType.TERMINATED;
    }

    public boolean isStarted() {
        return this.status == EventStatusType.STARTED;
    }

    public String getPrettyDateEvent() {
        ZonedDateTime result = dateEvent.atZone(ZoneId.systemDefault());
        return PrettyTime.of(Locale.FRANCE).printRelative(result);
    }

    @JsonIgnore
    public Set<Event> getPreviousEvents() {
        Set<Event> events = new LinkedHashSet<>();
        for (Quotation q : quotations) {
            events.addAll(q.getPreviousEvents());
        }
        return events;
    }

    private void scanEventHierarchy(int level, Event event, Map<Integer, Set<Event>> previousEvents) {
        Set<Event> previousEventsList = event.getPreviousEvents();
        if (previousEventsList.isEmpty()) {
            previousEvents.put(level, new LinkedHashSet<>(List.of(this)));
            return;
        }
        previousEvents.put(level, previousEventsList);
        for (Event e : previousEventsList) {
            scanEventHierarchy(level + 1, e, previousEvents);
        }
    }

    @JsonIgnore
    public Map<Integer, Set<Event>> getEventsHierarchy() {
        Map<Integer, Set<Event>> previousEvents = new HashMap<>();
        scanEventHierarchy(0, this, previousEvents);
        return previousEvents;
    }

    public void setDateEvent(LocalDateTime dateEvent) {
        this.dateEvent = dateEvent;
        if (this.dateEvent.isBefore(LocalDateTime.now())) {
            this.status = EventStatusType.TERMINATED;
        }
    }

    //Concatenate the scores of all athletes in the quotations of the event
    public String getScores() {
        if (!this.status.equals(EventStatusType.NOT_STARTED)) {
            return this.quotations.stream()
                    .map(q -> q.getScore() + "").collect(Collectors.joining(" - "));

        }
        return null;
    }

}


