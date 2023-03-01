package org.example.businesslogic;

import org.example.infrastructurelogic.InjectByType;

public class CoronaDesinfector {

    @InjectByType
    private Announcer announcer;

    @InjectByType
    private Policeman policeman;

    public void start(Room room) {
        // todo сообщить всем присутствующим в комнате о начале дезинфекции и попросить всех свалить
        announcer.announce("Объявляем дезинфекцию комнату");
        // todo разогнать всех кто не вышел после объявления
        policeman.makePeopleLeaveRoom();
        desinfect(room);
        announcer.announce("Рискните зайти обратно");
        // todo сообщить всем присутствующим в комнате, что они могут вернуться обратно
    }

    private void desinfect(Room room) {
        System.out.println("Комната дезинфицирована...");
    }
}
