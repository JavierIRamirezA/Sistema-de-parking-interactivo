# Configuración de rutas
JAVA_HOME ?= C:/Program Files/Java/jdk-24
JAVAC = "$(JAVA_HOME)/bin/javac"
JAVA = "$(JAVA_HOME)/bin/java"

SRC_DIR = src/main/java
RES_DIR = src/main/resources
OUT_DIR = out

JAVAFX_LIB = lib/javafx-sdk-24.0.1/lib
CONTROLSFX_JAR = lib/controlsfx-11.2.2.jar

MODULE = cl.ufsm.dpoo.sistemaparking
MAIN = cl.ufsm.dpoo.sistemaparking.Main

# Detección del sistema
ifeq ($(OS),Windows_NT)
  PATH_SEP = ;
  SOURCES = $(SRC_DIR)/module-info.java $(SRC_DIR)/cl/ufsm/dpoo/sistemaparking/*.java
else
  PATH_SEP = :
  SOURCES = $(shell find $(SRC_DIR) -name "*.java")
endif


MODULE_PATH = "$(JAVAFX_LIB)$(PATH_SEP)$(OUT_DIR)$(PATH_SEP)$(CONTROLSFX_JAR)"

# Reglas
all: compile

compile:
ifeq ($(OS),Windows_NT)
	if not exist "$(OUT_DIR)" mkdir "$(OUT_DIR)"
	xcopy /E /I /Y "$(RES_DIR)\*" "$(OUT_DIR)\"
else
	mkdir -p "$(OUT_DIR)"
	cp -r $(RES_DIR)/* "$(OUT_DIR)/"
endif
	$(JAVAC) --module-path $(MODULE_PATH) --add-modules javafx.controls,javafx.fxml,org.controlsfx.controls -d "$(OUT_DIR)" $(SOURCES)

run: compile
	$(JAVA) --module-path $(MODULE_PATH) --add-modules javafx.controls,javafx.fxml,org.controlsfx.controls -m $(MODULE)/$(MAIN)

clean:
ifeq ($(OS),Windows_NT)
	if exist "$(OUT_DIR)" rmdir /S /Q "$(OUT_DIR)"
else
	rm -rf "$(OUT_DIR)"
endif

.PHONY: all compile run clean
