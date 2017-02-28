## Build script

# Pull in Test Commons
echo "Pulling Test Commons."
mkdir src/
cd src
# Clear out old versions
rm -rf test-commons
# Create submodule
git submodule add --force git@github.com:iheartradio/test-commons.git
cd ..
git submodule init
git submodule update --remote

echo "Pulled most recent master changes for Test Commons. Remember to check out a Test Commons branch if you wish to make changes by cd'ing into the src/test-commons/ directory and doing git checkout -b <branchName>"


# Gradle Steps #
echo "Building project, getting dependencies, and creating eclipse project."
gradle build -x test
gradle eclipse

echo "Finished build."


# Properties Files #
echo "Copying properties file, please copy into appropriate location."
java -jar copyProperties.jar


# Password File #
echo "Decrypting passwords file."
java -jar encryptionTool.jar --decrypt --decryptedFile passwords/passwords.local --encryptedFile passwords/passwords.encrypted

echo "Setup complete!"


