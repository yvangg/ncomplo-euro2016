package org.jgayoso.ncomplo.business.services;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jgayoso.ncomplo.business.entities.BetType;
import org.jgayoso.ncomplo.business.entities.Competition;
import org.jgayoso.ncomplo.business.entities.repositories.BetTypeRepository;
import org.jgayoso.ncomplo.business.entities.repositories.CompetitionRepository;
import org.jgayoso.ncomplo.business.util.I18nNamedEntityComparator;
import org.jgayoso.ncomplo.business.util.IterableUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class BetTypeService {
    
    
    @Autowired
    private CompetitionRepository competitionRepository;

   
    @Autowired
    private BetTypeRepository betTypeRepository;
    
    
    
    public BetTypeService() {
        super();
    }
    
    
    @Transactional
    public BetType find(final Integer id) {
        return this.betTypeRepository.findOne(id);
    }
    
    
    @Transactional
    public List<BetType> findAllOrderByName(final Integer competitionId, final Locale locale) {
        final List<BetType> betTypes = 
                IterableUtils.toList(this.betTypeRepository.findByCompetitionId(competitionId));
        Collections.sort(betTypes, new I18nNamedEntityComparator(locale));
        return betTypes;
    }

    
    @Transactional
    public BetType save(
            final Integer id,
            final Integer competitionId,
            final String name,
            final Map<String,String> namesByLang,
            final String spec,
            final boolean sidesMatter,
            final boolean scoreMatter,
            final boolean resultMatter) {

        final Competition competition = 
                this.competitionRepository.findOne(competitionId);
                
        final BetType betType =
                (id == null? new BetType() : this.betTypeRepository.findOne(id));
        
        betType.setCompetition(competition);
        betType.setName(name);
        betType.getNamesByLang().clear();
        betType.getNamesByLang().putAll(namesByLang);
        betType.setSpec(spec);
        betType.setSidesMatter(sidesMatter);
        betType.setScoreMatter(scoreMatter);
        betType.setResultMatter(resultMatter);
        
        if (id == null) {
            competition.getBetTypes().add(betType);
            return this.betTypeRepository.save(betType);
        }
        return betType;
        
    }
    
    
    
    @Transactional
    public void delete(final Integer betTypeId) {
        
        final BetType betType = 
                this.betTypeRepository.findOne(betTypeId);
        final Competition competition = betType.getCompetition();
        
        competition.getBetTypes().remove(betType);
        
    }

    
}
