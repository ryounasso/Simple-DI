package main;

import javax.inject.Named;

@Named
public class Foo {
        String getMessage() {
            return "Hello";
        }
}
