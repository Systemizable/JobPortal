#!/bin/bash
set -e

# Install Java
echo "Installing Java..."
curl -s "https://get.sdkman.io" | bash

# Source SDKMAN
echo "Sourcing SDKMAN..."
source "/opt/render/.sdkman/bin/sdkman-init.sh"

# Install Java
echo "Installing Java 21..."
echo "Y" | sdk install java 21.0.2-open

# Explicitly set JAVA_HOME
echo "Setting JAVA_HOME..."
export JAVA_HOME="/opt/render/.sdkman/candidates/java/current"
export PATH="$JAVA_HOME/bin:$PATH"

# Print Java version to confirm installation
echo "Checking Java installation..."
java -version

# Make gradlew executable
echo "Making gradlew executable..."
chmod +x ./gradlew

# Run Gradle task with explicitly set JAVA_HOME
echo "Running Gradle task..."
./gradlew -Dorg.gradle.java.home="$JAVA_HOME" generateJavadoc

echo "JavaDoc generation complete!"