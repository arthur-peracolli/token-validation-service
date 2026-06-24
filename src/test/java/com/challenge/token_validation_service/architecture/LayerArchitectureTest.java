package com.challenge.token_validation_service.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class LayerArchitectureTest {

  private static JavaClasses classes;

  @BeforeAll
  static void setup() {
    classes = new ClassFileImporter().importPackages("com.challenge.token_validation_service");
  }

  @Test
  void domainShouldNotDependOnApplication() {

    noClasses()
        .that()
        .resideInAPackage("..domain..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..application..")
        .check(classes);
  }

  @Test
  void applicationShouldNotDependDirectlyOnInfrastructure() {

    noClasses()
        .that()
        .resideInAPackage("..application..")
        .should()
        .dependOnClassesThat()
        .resideInAPackage("..infrastructure..")
        .check(classes);
  }

  @Test
  void sharedShouldNotDependOnApplicationDomainOrInfrastructure() {

    noClasses()
        .that()
        .resideInAPackage("..shared..")
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage("..application..", "..domain..", "..infrastructure..")
        .check(classes);
  }
}
