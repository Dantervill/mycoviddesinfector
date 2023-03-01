package org.example.businesslogic;

import org.example.infrastructurelogic.InjectProperty;
import org.example.infrastructurelogic.Singleton;

@Singleton
@Deprecated
public class RecommendatorImpl implements Recommendator {

    @InjectProperty("wisky")
    private String alcohol;

    public RecommendatorImpl() {
        System.out.println("Recommendator was created");
    }

    @Override
    public void recommend() {
        System.out.println("to protect from covid-2019 drink " + alcohol);
    }
}
