package org.example.completableFuture;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class MethodReference {
  public static class Person {
    private final String name;
    public Person(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public Boolean compareTo(Person other) {
      return other.name.compareTo(name) > 0;
    }
  }

  public static void printName(String name) {
    System.out.println(name);
  }

  public static void main(String[] args) {
    var target = new Person("a");
    Consumer<String> staticPrint = MethodReference::printName; // static method reference

    Stream.of("d", "b", "c")
        .map(Person::new) // constructor reference
        .filter(target::compareTo) // method reference
        .map(Person::getName) // instance method reference
        .forEach(staticPrint); // static method reference
  }

}
