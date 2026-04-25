#!/usr/bin/env python3
"""
Generate comprehensive test methods for all check test classes
to cover AsyncAPI 2.6, 3.0, 3.1, and 3.2
"""

import os
import re
from pathlib import Path

# Directory containing test resources
test_resources_base = "src/test/resources/checks"
test_classes_dir = "src/test/java/org/sonar/samples/asyncapi/checks"

def get_test_files_for_check(rule_id):
    """Get all test files available for a rule across all versions"""
    test_files = {}

    for version in ['v2', 'v3', 'v31', 'v32']:
        version_path = Path(test_resources_base) / version
        if not version_path.exists():
            continue

        # Search for rule directories
        for category_dir in version_path.iterdir():
            if not category_dir.is_dir():
                continue

            rule_dir = category_dir / rule_id
            if not rule_dir.exists():
                continue

            # Get all yaml files in this directory
            files = list(rule_dir.glob('*.yaml'))
            if files:
                if version not in test_files:
                    test_files[version] = []

                # Extract just the filenames without extension
                for f in files:
                    filename = f.stem  # Removes .yaml extension
                    test_files[version].append(filename)

    return test_files

def generate_test_method(test_file, version):
    """Generate a test method for a given test file"""
    # Convert filename to method name
    # e.g., "with-https.yaml" -> "testWithHttps"
    parts = test_file.split('-')
    method_name = 'verify' + version.upper() + ''.join(word.capitalize() for word in parts)

    version_upper = version.upper()
    verify_method = f'verify{version_upper}'

    return f'''    @Test
    public void {method_name}() {{
        {verify_method}("{test_file}.yaml");
    }}
'''

def update_test_class(rule_id, category, test_files_dict):
    """Update a test class with new test methods"""
    # Find the test class
    test_class_path = None

    # Search for test class
    for root, dirs, files in os.walk(test_classes_dir):
        for file in files:
            if f'{rule_id}' in file and 'Test.java' in file:
                test_class_path = os.path.join(root, file)
                break

    if not test_class_path:
        print(f"⚠️  Could not find test class for {rule_id}")
        return False

    print(f"📝 Updating {rule_id} test class...")

    # Read the test class
    with open(test_class_path, 'r') as f:
        content = f.read()

    # Check if already has v3 tests
    if 'verifyV3' in content:
        print(f"   ℹ️  {rule_id} already has v3 tests, skipping...")
        return True

    # Find where to insert new methods (before @Override public void verifyRule())
    insert_position = content.rfind('@Override')
    if insert_position == -1:
        print(f"   ⚠️  Could not find insertion point in {rule_id}")
        return False

    # Generate new test methods
    new_methods = "\n    // ============= V3.0+ Tests =============\n"

    for version in ['v3', 'v31']:
        if version in test_files_dict:
            new_methods += f"\n    // --- {version.upper()} Tests ---\n"
            for test_file in sorted(test_files_dict[version]):
                new_methods += generate_test_method(test_file, version)

    # Insert the new methods
    content = content[:insert_position] + new_methods + "\n    " + content[insert_position:]

    # Write back
    with open(test_class_path, 'w') as f:
        f.write(content)

    print(f"   ✅ Updated {rule_id}")
    return True

def main():
    print("🚀 Updating test classes with comprehensive AsyncAPI 3.x tests\n")

    # Get all rule IDs
    rules = set()
    for version in ['v2', 'v3', 'v31', 'v32']:
        version_path = Path(test_resources_base) / version
        if not version_path.exists():
            continue

        for category_dir in version_path.iterdir():
            if not category_dir.is_dir():
                continue

            for rule_dir in category_dir.iterdir():
                if rule_dir.is_dir() and rule_dir.name.startswith('AAR'):
                    rules.add(rule_dir.name)

    updated = 0
    for rule_id in sorted(rules):
        test_files = get_test_files_for_check(rule_id)

        if test_files:
            # Get category (first version that has the rule)
            category = None
            for version in ['v2', 'v3', 'v31']:
                if version in test_files:
                    # Find category
                    for cat_dir in (Path(test_resources_base) / version).iterdir():
                        if (cat_dir / rule_id).exists():
                            category = cat_dir.name
                            break

            if category and update_test_class(rule_id, category, test_files):
                updated += 1

    print(f"\n✅ Updated {updated} test classes")
    print(f"📊 Total rules found: {len(rules)}")

if __name__ == '__main__':
    main()
