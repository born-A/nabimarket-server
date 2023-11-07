package org.prgrms.nabimarketbe.domain.suggestion.repository;

import org.prgrms.nabimarketbe.domain.suggestion.entity.Suggestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestionRepository extends JpaRepository<Suggestion, Long> {
}
