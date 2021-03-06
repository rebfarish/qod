package edu.cnm.deepdive.qod.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import edu.cnm.deepdive.qod.view.Flat;
import edu.cnm.deepdive.qod.view.Nested;
import java.net.URI;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;


@Component
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonView(Flat.class)
@Entity
public class Quote {

  private static EntityLinks entityLinks;

  @PostConstruct
  private void init(){
    String ignore = entityLinks.toString();
  }

  @Autowired
  private void setEntityLinks(EntityLinks entityLinks){
    Quote.entityLinks = entityLinks;
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)//This lets hibernate come up with values
  @Column(name = "quote_id", nullable = false, updatable = false)
  private long id;

  @NonNull//different from below. cannot pass nullable below is not store nullable
  @CreationTimestamp
  @Temporal(TemporalType.TIMESTAMP)
  @Column(nullable = false, updatable = false)//hibernate will enforce nonnullable, and create timestamp
  private Date created;

  @NonNull
  @Column(length = 4096, nullable = false)
  private String text;

  @NonNull
  @JsonView(Nested.class)
  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "source_id", nullable = false, updatable = false)
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Source source;

  public long getId() {
    return id;
  }

  public Date getCreated() {
    return created;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Source getSource() {
    return source;
  }

  public void setSource(Source source) {
    this.source = source;
  }

  public URI getHref(){
    return entityLinks.linkFor(Quote.class, source.getId())
        .slash(id)
        .toUri();
  }

}
