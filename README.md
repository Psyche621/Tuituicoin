## Adding to PATH

After running `python3 setup.py`, add the project directory to your system PATH to use `tuiranode` from anywhere.

### Linux & macOS

Edit your shell configuration file:

```bash
# For bash (most common):
echo 'export PATH="'$(pwd)':$PATH"' >> ~/.bashrc
source ~/.bashrc

# For zsh (newer macOS default):
echo 'export PATH="'$(pwd)':$PATH"' >> ~/.zshrc
source ~/.zshrc