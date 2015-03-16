require 'xcodeproj'

product_name = 'Generated'
project_file = product_name + '.xcodeproj'
project = Xcodeproj::Project.new(project_file)


file_name = 'EricTest.h'
group_name = product_name
file = project.new_file(file_name, group_name)


# Add the file to the main target
main_target = project.targets.first
main_target.add_file_references([file])

# Save the project file
project.save_as(project_file)
