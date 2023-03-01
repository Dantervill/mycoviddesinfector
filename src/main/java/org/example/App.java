package org.example;

import org.example.businesslogic.CoronaDesinfector;
import org.example.businesslogic.Policeman;
import org.example.businesslogic.PolicemanImpl;
import org.example.businesslogic.Room;
import org.example.infrastructurelogic.Application;
import org.example.infrastructurelogic.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main( String[] args ) {
        ApplicationContext context = Application.run("org.example", new HashMap<>(Map.of(Policeman.class, PolicemanImpl.class)));
        CoronaDesinfector coronaDesinfector = context.getObject(CoronaDesinfector.class);
        coronaDesinfector.start(new Room());
    }
}
