#
# enumeration_purse_types.rb
#
# Generated by JSON Model Generator v0.0.5 on Aug 28, 2016
# https://github.com/intere/generator-json-models
#
# The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
#

class EnumerationPurseTypes

    def subtitle
        @subtitle ||= json.try(:[], :subtitle)
    end

    def title
        @title ||= json.try(:[], :title)
    end


    def initialize(init_json)
        if init_json.class == String
            @json = JSON.parse(init_json)
        else
            @json = init_json
        end
        @json.symbolize_keys!
    end

    def json
        @json
    end
end
