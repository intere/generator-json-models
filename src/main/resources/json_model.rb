
#
#  json_model.rb
#
#  Released with JSON Model Generator ${version} on ${date}
#    https://github.com/intere/generator-json-models
#
#    The generator tool is licensed under the LGPL: http://www.gnu.org/licenses/lgpl-3.0.html#content
#
#
class JsonModel
  def initialize(json)
    if json.class == String
      json = JSON.parse(json)
    end
    @json = json
  end

  def json
    @json
  end
end
