package org.common.service;

import org.springframework.stereotype.Service;

@Service
public class AnalysisService implements IAnalysisService {

    public AnalysisService() {
        super();
    }

    // API

    @Override
    public final double determineSimillarity(final String from, final String to) {
        return 0.0;
    }

}
