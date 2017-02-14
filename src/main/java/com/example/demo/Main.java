package com.example.demo;

import org.wildfly.swarm.Swarm;

public class Main {

    private Main() {

    }

    public static void main(String... args) throws Exception {

        Swarm swarm = new Swarm();
        swarm.start().deploy();
    }

}
