import re
import sys

def make_xhtml(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()

    # Void elements in HTML that should be self-closing in XHTML
    void_elements = ['meta', 'link', 'input', 'hr', 'br', 'img']
    for tag in void_elements:
        pattern = rf'<{tag}([^>]*?)(?<!/)>'
        content = re.sub(pattern, rf'<{tag}\1 />', content)

    # Minimized attributes like required, autofocus, disabled, checked
    minimized_attrs = ['required', 'autofocus', 'disabled', 'checked', 'readonly', 'multiple', 'async', 'defer']
    for attr in minimized_attrs:
        # Match attr but not attr="..."
        # This is tricky because it might match inside other strings, 
        # but let's try matching it followed by a space or />
        pattern = rf'\b{attr}\b(?!\s*=\s*[\'"])'
        content = re.sub(pattern, f'{attr}="{attr}"', content)

    with open(file_path, 'w', encoding='utf-8') as f:
        f.write(content)
    print("XHTML conversion (with attributes) successful!")

if __name__ == "__main__":
    if len(sys.argv) > 1:
        make_xhtml(sys.argv[1])
    else:
        print("Please provide a file path.")
