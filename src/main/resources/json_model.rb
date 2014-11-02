
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
