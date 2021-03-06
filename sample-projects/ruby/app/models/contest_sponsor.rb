#
# contest_sponsor.rb
#
# Generated by JSON Model Generator v0.0.5 on Aug 28, 2016
# https://github.com/intere/generator-json-models
#
# The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
#

class ContestSponsor

    def address1
        @address1 ||= json.try(:[], :address1)
    end

    def address2
        @address2 ||= json.try(:[], :address2)
    end

    def category
        @category ||= json.try(:[], :category)
    end

    def city
        @city ||= json.try(:[], :city)
    end

    def contact_email
        @contact_email ||= json.try(:[], :contactEmail)
    end

    def contact_name
        @contact_name ||= json.try(:[], :contactName)
    end

    def contact_title
        @contact_title ||= json.try(:[], :contactTitle)
    end

    def id
        @id ||= json.try(:[], :id)
    end

    def logo
        @logo ||= json.try(:[], :logo)
    end

    def name
        @name ||= json.try(:[], :name)
    end

    def phone_number
        @phone_number ||= json.try(:[], :phoneNumber)
    end

    def site_url
        @site_url ||= json.try(:[], :siteUrl)
    end

    def state
        @state ||= json.try(:[], :state)
    end

    def zip
        @zip ||= json.try(:[], :zip)
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
